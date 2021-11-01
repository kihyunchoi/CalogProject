package com.example.calog.sleeping;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.common.Membership;
import com.example.calog.common.Navigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class SleepingActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    TimePicker alarmPicker;
    Context context;
    Button btnSleepStart;
    Button btnSleepFinish;
    ImageView btnBack;
    TextView txtDate;

    Intent intent;

    PendingIntent pendingIntent;

    BottomNavigationView bottomNavigationView;

    Retrofit retrofit;
    RemoteService rs;

    TextView userId;
    public static String strUserId;
    public static String selectedDate;
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
                membership.membershipStatus(item, SleepingActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeping);

        intent = getIntent();
        selectedDate = intent.getStringExtra("select_date");

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        View view1 = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view1 != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class);

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

        //Sleeping graph backthread
        SleepingGraph sleepingGraph = new SleepingGraph();
        sleepingGraph.execute();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSleepStart = findViewById(R.id.btnSleepStart);

        this.context = getApplicationContext();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmPicker = findViewById(R.id.timepicker);

        final Calendar calendar = Calendar.getInstance();

        final Intent alarmintent = new Intent(this.context, AlarmReceiver.class);

        btnSleepStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, alarmPicker.getHour());
                calendar.set(Calendar.MINUTE, alarmPicker.getMinute());

                int hour = alarmPicker.getHour();
                int minute = alarmPicker.getMinute();

                Intent gointent = new Intent(SleepingActivity.this, SleepCheckActivity.class);

                gointent.putExtra("user_id", strUserId);
                gointent.putExtra("select_date", txtDate.getText().toString());

                startActivity(gointent);
            }

        });

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_sleep_check, null);
        btnSleepFinish = view.findViewById(R.id.btnSleepFinish);
        btnSleepFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SleepingActivity.this, "Finish Alarm", Toast.LENGTH_SHORT).show();
                alarmManager.cancel(pendingIntent);

                alarmintent.putExtra("state", "alarm off");

                sendBroadcast(alarmintent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, SleepingActivity.this, strUserId, txtDate);
                return true;
            }
        });
    }
}



