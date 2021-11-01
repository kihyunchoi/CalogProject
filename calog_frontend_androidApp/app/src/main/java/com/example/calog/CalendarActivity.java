package com.example.calog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calog.common.Membership;
import com.example.calog.common.Navigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class CalendarActivity extends AppCompatActivity {

    ImageView btnBack;

    CalendarView calendarView;
    Intent intent;

    TextView userId;
    String strUserId;
    Toolbar toolbar;

    Retrofit retrofit;
    RemoteService rs;

    TextView txtDate;

    BottomNavigationView bottomNavigationView;

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
                membership.membershipStatus(item, CalendarActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        intent = getIntent();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userId = findViewById(R.id.user_id);

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        calendarView = (CalendarView) findViewById(R.id.calendar);
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

        long currentSelectedTime = intent.getLongExtra("currentSelectedTime", 0);

        if (currentSelectedTime != 0) {
            calendarView.setDate(currentSelectedTime);
        }

        /**
         * Keep the user select date
         */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                intent = new Intent(CalendarActivity.this, MainHealthActivity.class);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                intent.putExtra("currentSelectedTime", calendar.getTimeInMillis());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                finish();
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, CalendarActivity.this, strUserId, txtDate);
                return true;
            }
        });
    }
}
