package com.example.calog.diet;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.common.Membership;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.common.Navigation;
import com.example.calog.vo.UserDietTypeDailyCalorieSumVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class DietActivity extends AppCompatActivity {

    Intent intent;

    TextView txtBreakfast, txtLunch, txtDinner, txtSnack;
    TextView txtDate;
    Button btnDetailView;

    Retrofit retrofit;
    RemoteService rs;
    List<UserDietTypeDailyCalorieSumVO> dailyCalorie;

    ImageView btnBack;

    BottomNavigationView bottomNavigationView;

    TextView userId;
    public static String strUserId;
    public static String selectedDate;
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
                membership.membershipStatus(item, DietActivity.this,
                        pref, strUserId, userId));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        intent = getIntent();
        selectedDate = intent.getStringExtra("select_date");

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

        //Diet graph backthread
        DietGraph dietGraph = new DietGraph();
        dietGraph.execute();

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        txtBreakfast = findViewById(R.id.txtBreakfast);
        txtLunch = findViewById(R.id.txtLunch);
        txtDinner = findViewById(R.id.txtDinner);
        txtSnack = findViewById(R.id.txtSnack);

        //Diet detail button to see a list of daily menu list given selected day
        btnDetailView = findViewById(R.id.btnDetailView);
        btnDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(DietActivity.this, DietDailyDetailActivity.class);
                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", txtDate.getText().toString());

                startActivity(intent);
            }
        });

        SlidingDrawer dietDrawer = findViewById(R.id.dietDrawer);
        dietDrawer.animateClose();

        /**
         * Get a user diet daily calorie sum for each meal from database
         */
        Call<List<UserDietTypeDailyCalorieSumVO>> call = rs.userDietTypeDailyCalorieSum(strUserId, selectedDate);
        call.enqueue(new Callback<List<UserDietTypeDailyCalorieSumVO>>() {
            @Override
            public void onResponse(Call<List<UserDietTypeDailyCalorieSumVO>> call, Response<List<UserDietTypeDailyCalorieSumVO>> response) {
                dailyCalorie = new ArrayList<UserDietTypeDailyCalorieSumVO>();
                dailyCalorie = response.body();

                try {

                    txtBreakfast.setText("Breakfast :   " + dailyCalorie.get(0).getSum_calorie() + "kcal");
                    txtLunch.setText("Lunch :   " + dailyCalorie.get(1).getSum_calorie() + "kcal");
                    txtDinner.setText("Dinner :   " + dailyCalorie.get(2).getSum_calorie() + "kcal");
                    txtSnack.setText("Snack :   " + dailyCalorie.get(3).getSum_calorie() + "kcal");

                } catch (IndexOutOfBoundsException e) {
                    System.out.println("=====DietActivity DietTypeDailyCaloriesSum IndexOutOfBoundsException Error : " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<UserDietTypeDailyCalorieSumVO>> call, Throwable t) {
                System.out.println("=====DietActivity DietTypeDailyCaloriesSum onFailure Error : " + t.toString());
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, DietActivity.this, strUserId, txtDate);
                return true;
            }
        });
    }

    /**
     * Insert into database for a user specific meal type and selected date
     *
     * @param view
     */
    public void mClick(View view) {
        intent = new Intent(DietActivity.this, FoodRegisterActivity.class);
        switch (view.getId()) {
            case R.id.btnBreakfast:
                Toast.makeText(DietActivity.this, "Breakfast", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", txtDate.getText().toString());
                intent.putExtra("diet_type_id", 1);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.btnLunch:
                Toast.makeText(DietActivity.this, "Lunch", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", txtDate.getText().toString());
                intent.putExtra("diet_type_id", 2);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.btnDinner:
                Toast.makeText(DietActivity.this, "Dinner", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", txtDate.getText().toString());
                intent.putExtra("diet_type_id", 3);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.btnSnack:
                Toast.makeText(DietActivity.this, "Snack", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", txtDate.getText().toString());
                intent.putExtra("diet_type_id", 4);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Call<List<UserDietTypeDailyCalorieSumVO>> call = rs.userDietTypeDailyCalorieSum(strUserId, selectedDate);
        call.enqueue(new Callback<List<UserDietTypeDailyCalorieSumVO>>() {
            @Override
            public void onResponse(Call<List<UserDietTypeDailyCalorieSumVO>> call, Response<List<UserDietTypeDailyCalorieSumVO>> response) {
                dailyCalorie = new ArrayList<UserDietTypeDailyCalorieSumVO>();
                dailyCalorie = response.body();

                try {

                    txtBreakfast.setText("Breakfast :   " + dailyCalorie.get(0).getSum_calorie() + "kcal");
                    txtLunch.setText("Lunch :   " + dailyCalorie.get(1).getSum_calorie() + "kcal");
                    txtDinner.setText("Dinner :   " + dailyCalorie.get(2).getSum_calorie() + "kcal");
                    txtSnack.setText("Snack :   " + dailyCalorie.get(3).getSum_calorie() + "kcal");

                } catch (IndexOutOfBoundsException e) {
                    System.out.println("=====DietActivity onNewIntent IndexOutOfBoundsException: " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<UserDietTypeDailyCalorieSumVO>> call, Throwable t) {
                System.out.println("=====DietActivity onNewIntent onFailure: " + t.toString());
            }
        });
    }
}
