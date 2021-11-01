package com.example.calog;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.calog.common.Membership;
import com.example.calog.common.Navigation;
import com.example.calog.diet.DietActivity;
import com.example.calog.fitness.FitnessActivity;
import com.example.calog.sleeping.SleepingActivity;
import com.example.calog.vo.MainHealthVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class MainHealthActivity extends AppCompatActivity {
    RelativeLayout btnDiet, btnFitness, btnSleep;
    TextView txtDate;
    ImageView imgDiet, imgFitness, imgSleep;
    TextView txtEatCalorie, txtUsedCalorie, txtSleepHours;

    TextView userId;
    String strUserId;
    Toolbar toolbar;

    MainHealthVO userVO;

    Intent intent;

    HorizontalCalendar horizontalCalendar;

    private static final int MY_PERMISSIONS_REQUEST_RINGSTONE = 1003;

    long currentSelectedTime = 0;

    BottomNavigationView bottomNavigationView;

    Retrofit retrofit;
    RemoteService rs;

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
                membership.membershipStatus(item, MainHealthActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_health);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        pref = getSharedPreferences("pjLogin", MODE_PRIVATE);
        intent = getIntent();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtEatCalorie = findViewById(R.id.txtEatCalorie);
        txtEatCalorie.setText("Calorie Intake : " + 0 + "kcal");

        txtUsedCalorie = findViewById(R.id.txtUsedCalorie);
        txtUsedCalorie.setText("Consumed Calories : " + 0 + "kcal");

        txtSleepHours = findViewById(R.id.txtSleepHours);
        txtSleepHours.setText("Sleeping Hours : " + 0 + "hours");

        imgDiet = findViewById(R.id.imgDiet);
        imgFitness = findViewById(R.id.imgFitness);
        imgSleep = findViewById(R.id.imgSleep);

        userId = findViewById(R.id.user_id);

        permissionCheck();

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

        if (userVO == null) {
            imgDiet.setBackgroundResource(R.drawable.ic_neutral);
            imgFitness.setBackgroundResource(R.drawable.ic_neutral);
            imgSleep.setBackgroundResource(R.drawable.ic_neutral);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        txtDate = findViewById(R.id.txtDate);

        intent = getIntent();
        txtDate.setText(intent.getStringExtra("date"));

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainHealthActivity.this, CalendarActivity.class);
                intent.putExtra("currentSelectedTime", currentSelectedTime);

                Date longDate = new Date(currentSelectedTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = dateFormat.format(longDate);

                intent.putExtra("select_date", selectedDate);

                startActivity(intent);
            }
        });

        //Diet Button
        btnDiet = findViewById(R.id.btnDiet);
        btnDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainHealthActivity.this, DietActivity.class);

                Date longDate = new Date(currentSelectedTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = dateFormat.format(longDate);

                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", selectedDate);

                startActivity(intent);
            }
        });

        //Fitness Button
        btnFitness = findViewById(R.id.btnFitness);
        btnFitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainHealthActivity.this, FitnessActivity.class);

                Date longDate = new Date(currentSelectedTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = dateFormat.format(longDate);

                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", selectedDate);

                startActivity(intent);
            }
        });

        //Sleep Button
        btnSleep = findViewById(R.id.btnSleep);
        btnSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainHealthActivity.this, SleepingActivity.class);

                Date longDate = new Date(currentSelectedTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = dateFormat.format(longDate);

                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", selectedDate);

                startActivity(intent);
            }
        });

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 5);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR, -5);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .textSize(10f, 20f, 10f)
                .showDayName(true)
                .showMonthName(true)
                .build();

        //Calendar date Change
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                java.sql.Date datesql = new java.sql.Date(date.getTime());
                currentSelectedTime = datesql.getTime();

                /**
                 * Show a user selected date diet total calorie intake,
                 * fitness calorie consumption, and sleeping hours from the database
                 */
                Call<MainHealthVO> call =
                        rs.userMainHealthData("spider", String.valueOf(datesql));
                call.enqueue(new Callback<MainHealthVO>() {
                    @Override
                    public void onResponse(Call<MainHealthVO> call, Response<MainHealthVO> response) {
                        userVO = response.body();

                        if (userVO == null) {
                            txtEatCalorie.setText("Calorie Intake : " + 0 + "kcal");
                            txtUsedCalorie.setText("Calorie Consumption : " + 0 + "kcal");
                            txtSleepHours.setText("Sleeping Hours : " + 0 + "hours");
                        } else {
                            int hour = userVO.getSleeping_seconds() / 3600;
                            int minute = userVO.getSleeping_seconds() % 3600 / 60;
                            int second = userVO.getSleeping_seconds() % 3600 % 60;

                            int dietSum = (int) (userVO.getSum_diet_calorie());
                            int fitnessSum = (int) (userVO.getSum_fitness_calorie());

                            txtEatCalorie.setText("Calorie Intake : " + dietSum + "kcal");
                            txtUsedCalorie.setText("Calorie Consumption : " + fitnessSum + "kcal");
                            txtSleepHours.setText("Sleeping : " + hour + "h " + minute + "m " + second + "s");

                            if (userVO.getSum_diet_calorie() < 1500 || userVO.getSum_diet_calorie() > 3000) {
                                imgDiet.setBackgroundResource(R.drawable.ic_bad);
                            } else if (userVO.getSum_diet_calorie() == 0.0 || userVO == null) {
                                imgDiet.setBackgroundResource(R.drawable.ic_neutral);
                            } else if (userVO.getSum_diet_calorie() >= 2000 && userVO.getSum_diet_calorie() <= 2500) {
                                imgDiet.setBackgroundResource(R.drawable.ic_good);
                            } else {
                                imgDiet.setBackgroundResource(R.drawable.ic_neutral);
                            }

                            if (userVO.getSum_fitness_calorie() < 300 || userVO.getSum_fitness_calorie() > 1500) {
                                imgFitness.setBackgroundResource(R.drawable.ic_bad);
                            } else if (userVO.getSum_fitness_calorie() == 0.0 || userVO == null) {
                                imgFitness.setBackgroundResource(R.drawable.ic_neutral);
                            } else if (userVO.getSum_fitness_calorie() >= 500 && userVO.getSum_fitness_calorie() <= 1000) {
                                imgFitness.setBackgroundResource(R.drawable.ic_good);
                            } else {
                                imgFitness.setBackgroundResource(R.drawable.ic_neutral);
                            }

                            if (userVO.getSleeping_seconds() < 18000 || userVO.getSleeping_seconds() > 36000) {
                                imgSleep.setBackgroundResource(R.drawable.ic_bad);
                            } else if (userVO.getSleeping_seconds() == 0 || userVO == null) {
                                imgSleep.setBackgroundResource(R.drawable.ic_neutral);
                            } else if (userVO.getSleeping_seconds() >= 25200 && userVO.getSleeping_seconds() <= 28800) {
                                imgSleep.setBackgroundResource(R.drawable.ic_good);
                            } else {
                                imgSleep.setBackgroundResource(R.drawable.ic_neutral);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MainHealthVO> call, Throwable t) {
                        System.out.println("=====MainHealthActivity userMainHealthData onFailure: " + t.toString());
                    }
                });
                txtDate.setText(DateFormat.getDateInstance().format(date));

                currentSelectedTime = date.getTime();
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, MainHealthActivity.this, strUserId, txtDate);
                return true;
            }
        });
    }

    /**
     * Permission check
     */
    public void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        int permssionCheckRingstone = ActivityCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK);

        if (permssionCheckRingstone != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WAKE_LOCK)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WAKE_LOCK},
                        MY_PERMISSIONS_REQUEST_RINGSTONE);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        currentSelectedTime = intent.getLongExtra("currentSelectedTime", 0);

        java.sql.Date date = new java.sql.Date(currentSelectedTime);

        txtDate.setText(DateFormat.getDateInstance().format(date));
        horizontalCalendar.selectDate(date, true);
    }
}

