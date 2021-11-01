package com.example.calog.fitness;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.common.Membership;
import com.example.calog.common.Navigation;
import com.example.calog.vo.UserFitnessTotalCaloriesViewVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class FitnessActivity extends AppCompatActivity {
    RelativeLayout btnFitnessTrainingActivity;
    ImageView btnBack;
    Intent intent;
    TextView txtDate, txtFitnessCalorie, txtFitnessTime;
    ImageView fitnessList;
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
                membership.membershipStatus(item, FitnessActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        intent = getIntent();
        selectedDate = intent.getStringExtra("select_date");

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

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(selectedDate);

        //Fitness graph backthread
        FitnessGraph fitnessGraph = new FitnessGraph();
        fitnessGraph.execute();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFitnessTrainingActivity = findViewById(R.id.btnFitnessTrainingActivity);
        btnFitnessTrainingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchActivity();
            }
        });

        fitnessList = findViewById(R.id.fitnessList);
        fitnessList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyFitnessList();
            }
        });

        txtFitnessCalorie = findViewById(R.id.fitnessCalorie);
        txtFitnessTime = findViewById(R.id.fitnessTime);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        /**
         * Get a daily fitness time and total calorie from database
         */
        Call<UserFitnessTotalCaloriesViewVO> weightCall = rs.userFitnessDailyTotalCalorie(strUserId, selectedDate);
        weightCall.enqueue(new Callback<UserFitnessTotalCaloriesViewVO>() {
            @Override
            public void onResponse(Call<UserFitnessTotalCaloriesViewVO> call, Response<UserFitnessTotalCaloriesViewVO> response) {
                UserFitnessTotalCaloriesViewVO vo = response.body();
                txtFitnessCalorie.setText((vo.getFitness_sum_calorie_consumption() + "kcal"));

                long fitnessTime = vo.getFitness_sum_seconds();
                int fth = (int) (fitnessTime / 3600);
                int ftm = (int) (fitnessTime - fth * 3600) / 60;
                int fts = (int) (fitnessTime - fth * 3600 - ftm * 60);
                String strH = fth < 10 ? "0" + fth : fth + "";
                String strM = ftm < 10 ? "0" + ftm : ftm + "";
                String strS = fts < 10 ? "0" + fts : fts + "";
                txtFitnessTime.setText(strH + "h " + strM + "m " + strS + "s");

            }

            @Override
            public void onFailure(Call<UserFitnessTotalCaloriesViewVO> call, Throwable t) {
                System.out.println("=====FitnessActivity oneDayWeightTotalCalorie onFailure: " + t.toString());

            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, FitnessActivity.this, strUserId, txtDate);
                return true;
            }
        });
    }

    /**
     * Search fitness menu list to exercise
     */
    public void goToSearchActivity() {
        intent = new Intent(FitnessActivity.this, SearchFitnessActivity.class);
        intent.putExtra("user_id", strUserId);
        intent.putExtra("select_date", selectedDate);
        startActivity(intent);
    }

    /**
     * My fitness menu list for selected list
     */
    public void goToMyFitnessList() {
        intent = new Intent(FitnessActivity.this, MyFitnessListActivity.class);
        intent.putExtra("select_date", selectedDate);
        intent.putExtra("user_id", strUserId);
        startActivity(intent);
    }
}
