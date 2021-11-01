package com.example.calog.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.common.Membership;
import com.example.calog.common.Navigation;
import com.example.calog.vo.FitnessVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class ExerciseActivity extends AppCompatActivity {
    ImageView btnBack;
    Intent intent;
    Chronometer timeElapse;

    int fitnessMenuId;
    double fitnessUnitCalorie;
    Retrofit retrofit;
    RemoteService rs;

    int fitnessSeconds;
    double usedCalorie;

    long time;
    long stopTime = 0;
    int h;
    int m;
    int s;
    String hh;
    String mm;
    String ss;
    BackThread thread;

    TextView txtCalorie, txtDate;

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
                membership.membershipStatus(item, ExerciseActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        intent = getIntent();
        fitnessMenuId = intent.getIntExtra("fitness_menu_id", 0);
        fitnessUnitCalorie = intent.getDoubleExtra("unit_calorie", 0.0);
        strUserId = intent.getStringExtra("user_id");

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

        openImageFrame();
        openButtonFrame();
        txtCalorie = findViewById(R.id.usedCalorie);
        thread = new BackThread();
        thread.setDaemon(true);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        timeElapse = (Chronometer) findViewById(R.id.chronometer);
        timeElapse.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                time = SystemClock.elapsedRealtime() - chronometer.getBase();
                h = (int) (time / 36000000);
                m = (int) (time - h * 3600000) / 60000;
                s = (int) (time - h * 3600000 - m * 60000) / 1000;
                hh = h < 10 ? "0" + h : h + "";
                mm = m < 10 ? "0" + m : m + "";
                ss = s < 10 ? "0" + s : s + "";
                chronometer.setText(hh + ":" + mm + ":" + ss);
            }
        });
        timeElapse.setText("00:00:00");

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, ExerciseActivity.this, strUserId, txtDate);
                return true;
            }
        });
    }

    class BackThread extends Thread {
        public void run() {
            while (true) {
                calorieCalculator();
                handler.sendEmptyMessage(0);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String strCalorie = String.format("%.1f", usedCalorie);
                txtCalorie.setText("Consumed Calories : " + strCalorie + " kcal");
            }
            super.handleMessage(msg);
        }
    };


    /**
     * Start, Stop, Continue, and Finish button
     *
     * @param view
     */
    public void mClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr;

        switch (view.getId()) {
            case R.id.btnStart:
                timeElapse.setBase(SystemClock.elapsedRealtime());
                btnStartandStop();
                timeElapse.start();
                thread.start();
                break;

            case R.id.btnStop:
                timeElapse.stop();
                stopTime = SystemClock.elapsedRealtime() - timeElapse.getBase();
                tr = fm.beginTransaction();
                Fitness_Fragment_StopWatchContinue fragmentContinue = new Fitness_Fragment_StopWatchContinue();
                tr.replace(R.id.btnFrame, fragmentContinue, "Continue");
                tr.commit();
                break;

            case R.id.btnContinue:
                btnStartandStop();
                timeElapse.setBase(SystemClock.elapsedRealtime() - stopTime);
                timeElapse.start();
                break;

            case R.id.btnFinish:

                final LinearLayout resultLayout = (LinearLayout) View.inflate(ExerciseActivity.this, R.layout.result_exercise, null);
                final AlertDialog.Builder resultBox = new AlertDialog.Builder(ExerciseActivity.this);

                TextView txtResultTime = resultLayout.findViewById(R.id.resultTime);
                txtResultTime.setText((hh) + "h " + (mm) + "m " + (ss) + "s");
                TextView txtTotalUsedCalorie = resultLayout.findViewById(R.id.totalUsedCal);
                String strCalorie = String.format("%.1f", usedCalorie);
                txtTotalUsedCalorie.setText(strCalorie + "kcal");
                resultBox.setIcon(R.drawable.ic_fitness_center_black_24dp);
                resultBox.setTitle("Fitness Result");
                resultBox.setView(resultLayout);
                resultBox.setNegativeButton("Cancel", null);
                resultBox.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        insertData();
                        intent = new Intent(ExerciseActivity.this, FitnessActivity.class);
                        intent.putExtra("select_date", txtDate.getText().toString());
                        intent.putExtra("user_id", strUserId);
                        startActivity(intent);

                    }
                });
                resultBox.show();
                break;
        }
    }

    /**
     * Start or Stop button change fragment
     */
    public void btnStartandStop() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        Fitness_Fragment_StopWatchStop fragmentStop = new Fitness_Fragment_StopWatchStop();
        tr.replace(R.id.btnFrame, fragmentStop, "Pause");
        tr.commit();
    }

    /**
     * Fitness gif image fragment
     */
    public void openImageFrame() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        Fitness_Fragment_GIF fragment_gif = new Fitness_Fragment_GIF(fitnessMenuId);
        tr.add(R.id.exerciseFrame, fragment_gif, "gif");
        tr.commit();
    }

    /**
     * Stopwatch button change fragment
     */
    public void openButtonFrame() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        Fitness_Fragment_StopWatchStart fragmentStart = new Fitness_Fragment_StopWatchStart();
        tr.add(R.id.btnFrame, fragmentStart, "Start");
        tr.commit();
    }

    /**
     * Calorie calculator
     */
    public void calorieCalculator() {
        fitnessSeconds = (int) time / 1000;
        usedCalorie = fitnessSeconds * fitnessUnitCalorie;
    }

    /**
     * Insert Fitness data into the database
     */
    public void insertData() {

        FitnessVO vo = new FitnessVO();
        vo.setUser_id(strUserId);
        vo.setFitness_date(intent.getStringExtra("select_date"));
        vo.setFitness_menu_id(fitnessMenuId);
        vo.setFitness_seconds(fitnessSeconds);
        vo.setCalorie_consumption(usedCalorie);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        Call<Void> fitnessInsert = rs.userFitnessInsert(vo);
        fitnessInsert.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ExerciseActivity.this, "Saved Fitness Weight Record.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ExerciseActivity.this, "Saving Failure.", Toast.LENGTH_SHORT).show();
                System.out.println("=====ExerciseActivity userWeightInsert onFailure" + t.toString());
            }
        });
    }
}
