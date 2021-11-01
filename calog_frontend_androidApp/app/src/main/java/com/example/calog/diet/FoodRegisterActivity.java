package com.example.calog.diet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.example.calog.common.Membership;
import com.example.calog.common.Navigation;
import com.example.calog.vo.DietMenuVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FoodRegisterActivity extends AppCompatActivity {

    ImageView btnBack;
    Intent intent;
    ArrayList<DietMenuVO> myList;
    TabLayout tabLayout;
    ViewPager foodListPager;
    PagerAdapter pagerAdapter;
    BundleAdapter bundleAdapter;
    Bundle bundle;

    TextView txtDate;

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
                membership.membershipStatus(item, FoodRegisterActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_register);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        pageTrans();

        intent = getIntent();

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.addTab(tabLayout.newTab().setText("My Favorite Food List"));
        tabLayout.addTab(tabLayout.newTab().setText("My Food List"));
        myList = new ArrayList<>();

        bundleAdapter = new BundleAdapter(this, myList);
        bundle = new Bundle();
        bundle.putSerializable("bundleAdapter", bundleAdapter);
        bundle.putParcelableArrayList("myList", myList);

        foodListPager = findViewById(R.id.foodListPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        foodListPager.setAdapter(pagerAdapter);
        foodListPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        foodListPager.setCurrentItem(0);

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

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                foodListPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, FoodRegisterActivity.this, strUserId, txtDate);
                return true;
            }
        });
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FoodSearchFragment foodSearchFragemt = new FoodSearchFragment();
                    foodSearchFragemt.setArguments(bundle);
                    return foodSearchFragemt;
                case 1:
                    MyFavoriteFoodFragment myFavoriteFoodFragment = new MyFavoriteFoodFragment();
                    myFavoriteFoodFragment.setArguments(bundle);
                    return myFavoriteFoodFragment;
                case 2:
                    MyFoodFragment myFoodFragment = new MyFoodFragment();
                    myFoodFragment.setArguments(bundle);
                    return myFoodFragment;
            }
            return null;
        }

        public int getCount() {
            return 3;
        }
    }

    public void pageTrans() {

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
