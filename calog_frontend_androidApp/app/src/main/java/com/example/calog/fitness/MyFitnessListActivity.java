package com.example.calog.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.common.Membership;
import com.example.calog.common.Navigation;
import com.example.calog.vo.UserFitnessViewVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class MyFitnessListActivity extends AppCompatActivity {
    TextView myListTitle, txtDate;
    ImageView btnBack;
    RecyclerView list;
    List<UserFitnessViewVO> array;
    MyFitnessListAdapter adapter;

    Intent intent;
    Retrofit retrofit;
    RemoteService rs;
    String str_user_id;
    String fitnessDate;

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
                membership.membershipStatus(item, MyFitnessListActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fitness_list);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        myListTitle = findViewById(R.id.myListTitle);
        intent = getIntent();
        str_user_id = intent.getStringExtra("user_id");
        fitnessDate = intent.getStringExtra("select_date");

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(fitnessDate);

        list = findViewById(R.id.OnedayFitnessList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        myListTitle.setText("Fitness Detail " + fitnessDate);
        connect();

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, MyFitnessListActivity.this, strUserId, txtDate);
                return true;
            }
        });

    }

    public void connect() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        /**
         * Show a user fitness daily menu list from the database by given selected date
         */
        Call<List<UserFitnessViewVO>> userFitnessDailyMenuList = rs.userFitnessDailyMenuList(str_user_id, fitnessDate);
        userFitnessDailyMenuList.enqueue(new Callback<List<UserFitnessViewVO>>() {
            @Override
            public void onResponse(Call<List<UserFitnessViewVO>> call, Response<List<UserFitnessViewVO>> response) {
                array = response.body();
                adapter = new MyFitnessListAdapter(MyFitnessListActivity.this, array);
                list.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<UserFitnessViewVO>> call, Throwable t) {
                System.out.println("=====MyFitnessListActivity connect oneDayWeightList onFailure: " + t.toString());

            }
        });
    }
}
