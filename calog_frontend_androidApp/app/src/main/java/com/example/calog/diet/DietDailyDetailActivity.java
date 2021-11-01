package com.example.calog.diet;

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
import com.example.calog.vo.UserDietViewVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class DietDailyDetailActivity extends AppCompatActivity {

    RecyclerView userDietDailyMenuList;

    Intent intent;

    Retrofit retrofit;
    RemoteService rs;
    DietDailyMenuAdapter adapter;
    List<UserDietViewVO> userDietDailyMenuArray;

    ImageView btnBack;

    TextView txtDate;

    BottomNavigationView bottomNavigationView;

    TextView userId;
    String strUserId;
    Toolbar toolbar;
    SharedPreferences pref;
    boolean SignInStatus = false;

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

        return super.onPrepareOptionsMenu(membership.SignInStatus(menu, SignInStatus));
    }

    //Member signIn, signOut, update, or delete
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        membership = new Membership();

        return super.onOptionsItemSelected(
                membership.membershipStatus(item, DietDailyDetailActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_daily_detail);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

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
            SignInStatus = false;
        } else {
            userId.setText("Welcome to " + strUserId);
            SignInStatus = true;
        }

        userDietDailyMenuList = findViewById(R.id.userDietDailyMenuList);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        userDietDailyMenuList.setLayoutManager(manager);

        intent = getIntent();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        //Show a user diet daily menu list for each meal
        Call<List<UserDietViewVO>> call = rs.userDietDailyMenuList(strUserId, intent.getStringExtra("select_date"));
        call.enqueue(new Callback<List<UserDietViewVO>>() {
            @Override
            public void onResponse(Call<List<UserDietViewVO>> call, Response<List<UserDietViewVO>> response) {
                userDietDailyMenuArray = response.body();
                System.out.println("=====DietDailyDetailActivity userDietDailyMenuList onResponse userDietDailyMenuArray.size: " + userDietDailyMenuArray.size());
                adapter = new DietDailyMenuAdapter(DietDailyDetailActivity.this, userDietDailyMenuArray);
                userDietDailyMenuList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<UserDietViewVO>> call, Throwable t) {
                System.out.println("=====DietDailyDetailActivity userDietDailyMenuList onFailure: " + t.toString());
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, DietDailyDetailActivity.this, strUserId, txtDate);
                return true;
            }
        });

    }
}
