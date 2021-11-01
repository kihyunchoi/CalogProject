package com.example.calog.sleeping;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.common.Membership;
import com.example.calog.common.Navigation;
import com.example.calog.vo.SleepingVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class SleepCheckActivity extends AppCompatActivity {
    ImageView btnBack;
    Retrofit retrofit;
    RemoteService rs;

    TextView txtDate;

    TextView timeset;
    int timeput = 0;

    Handler timehandler = new Handler() {
        String stringTimer = "00:00:00";

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                stringTimer = String.format("%02d:%02d:%02d", timeput / (60 * 60), (timeput / 60) % 60, (timeput % 60));
                timeset.setText(stringTimer);
            }
        }
    };

    Intent intent;

    int currentSleepingSeconds;

    BottomNavigationView bottomNavigationView;

    TextView userId;
    String strUserId;
    Toolbar toolbar;
    SharedPreferences pref;
    boolean signInStatus = false;

    Membership membership;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.loginmenu, menu);
        return true;
    }

    //Membership menu visibility according to the user SignInStatus
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        membership = new Membership();

        return super.onPrepareOptionsMenu(membership.SignInStatus(menu, signInStatus));
    }

    //Member signIn, signOut, update, or delete
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        membership = new Membership();

        return super.onOptionsItemSelected(
                membership.membershipStatus(item, SleepCheckActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_check);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent = getIntent();

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userId = findViewById(R.id.user_id);
        pref = getSharedPreferences("pjLogin", MODE_PRIVATE);

        //Check user logIn status and get the user id from shared preferences
        strUserId = pref.getString("user_id", "");
        userId.setText(strUserId);

        if (strUserId.equals("")) {
            userId.setText("");
            signInStatus = false;
        } else {
            userId.setText("Welcome to " + strUserId);
            signInStatus = true;
        }

        timeset = (TextView) findViewById(R.id.Timer);
        final TimeCatch timecatch = new TimeCatch();

        timecatch.setDaemon(true);
        timecatch.start();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = formatter.format(date);

        rs = retrofit.create(RemoteService.class);
        Call<SleepingVO> call = rs.userSleepingDailyInfo(strUserId, today);
        call.enqueue(new Callback<SleepingVO>() {
            @Override
            public void onResponse(Call<SleepingVO> call, Response<SleepingVO> response) {
                SleepingVO vo = response.body();
                currentSleepingSeconds = vo.getSleeping_seconds();
            }

            @Override
            public void onFailure(Call<SleepingVO> call, Throwable t) {
                System.out.println("=====SleepCheckActivity userSleepingDailyInfo onFailure: " + t.getStackTrace());
            }
        });

        Button btnSleepFinish = (Button) findViewById(R.id.btnSleepFinish);
        btnSleepFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimeCatch.interrupted();

                AlertDialog.Builder builder = new AlertDialog.Builder(SleepCheckActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.activity_sleep_check_result, null);

                final TextView totalSleep = (TextView) view.findViewById(R.id.totalSleep);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        intent = getIntent();

                        int sleepSeconds = timeput;

                        SleepingVO vo = new SleepingVO();
                        vo.setUser_id(strUserId);
                        vo.setSleeping_seconds(currentSleepingSeconds + sleepSeconds);

                        /**
                         * Insert sleep result into the database
                         */
                        Call<Void> call1 = rs.sleepResultInsert(vo);
                        call1.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(SleepCheckActivity.this, "Saved.", Toast.LENGTH_SHORT).show();

                                intent = new Intent(SleepCheckActivity.this, MainHealthActivity.class);
                                intent.putExtra("select_date", txtDate.getText().toString());
                                intent.putExtra("user_id", strUserId);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(SleepCheckActivity.this, "Save Failed", Toast.LENGTH_SHORT).show();
                                System.out.println("=====SleepCheckActivity sleepResultInsert onFailure: " + t.getStackTrace());
                            }
                        });
                    }
                });

                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SleepCheckActivity.this, MainHealthActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setView(view);

                long time = timeput;
                int hour = (int) (time / 3600);
                int minute = (int) (time % 3600 / 60);
                int second = (int) (time % 3600 % 60);
                totalSleep.setText("Total Sleeping Hours: " + String.valueOf(hour + "h" + minute + "m" + second + "s"));

                builder.show();
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, SleepCheckActivity.this, strUserId, txtDate);
                return true;
            }
        });
    }

    class TimeCatch extends Thread {
        public void run() {
            while (true) {
                timeput++;
                timehandler.sendEmptyMessage(0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
