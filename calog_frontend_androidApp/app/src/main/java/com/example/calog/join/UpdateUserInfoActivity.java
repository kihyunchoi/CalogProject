package com.example.calog.join;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.common.Membership;
import com.example.calog.common.Navigation;
import com.example.calog.vo.UserVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class UpdateUserInfoActivity extends AppCompatActivity {

    RemoteService rs;
    UserVO user;
    Retrofit retrofit;

    Intent intent;

    Button btnSave, btnCancel;

    ImageView btnBack;

    EditText userId, password, confirmPassword, email, name,
            phone, birthday, gender, height, weight, bmi;

    TextView txtDate;

    BottomNavigationView bottomNavigationView;

    TextView txtUserId;
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
                membership.membershipStatus(item, UpdateUserInfoActivity.this,
                        pref, strUserId, userId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        intent = getIntent();

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        userId = (EditText) findViewById(R.id.txt_user_id);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        birthday = findViewById(R.id.birthday);
        gender = (EditText) findViewById(R.id.gender);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        bmi = findViewById(R.id.bmi);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        intent = getIntent();

        userId.setText(intent.getStringExtra("user_id"));
        password.setText(intent.getStringExtra("password"));
        email.setText(intent.getStringExtra("email"));
        name.setText(intent.getStringExtra("name"));
        phone.setText(intent.getStringExtra("phone"));
        birthday.setText(intent.getStringExtra("birthday"));
        gender.setText(intent.getStringExtra("gender"));
        height.setText(String.valueOf(intent.getDoubleExtra("height", 0.0)));
        weight.setText(String.valueOf(intent.getDoubleExtra("weight", 0.0)));
        bmi.setText(String.valueOf(intent.getDoubleExtra("bmi", 0.0)));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtUserId = findViewById(R.id.user_id);
        pref = getSharedPreferences("pjLogin", MODE_PRIVATE);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        //Check user logIn status and get the user id from shared preferences
        strUserId = pref.getString("user_id", "");
        txtUserId.setText(strUserId);

        if (strUserId.equals("")) {
            txtUserId.setText("");
            signInStatus = false;
        } else {
            txtUserId.setText("Welcome to " + strUserId);
            signInStatus = true;
        }

        /* === Check Text condition(change text color and toast according to condition) START === */
        //User id
        txtUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtUserId.getText().toString().trim().length() <= 20) {
                    txtUserId.setTextColor(Color.BLACK);
                } else {
                    txtUserId.setTextColor(Color.RED);
                    Toast.makeText(UpdateUserInfoActivity.this, "Please input user id",
                            Toast.LENGTH_LONG).show();
                    txtUserId.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Password
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // If the password length is less than 4 letters, keep the color red, otherwise, black
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.getText().toString().length() >= 4 && password.getText().toString().equals(confirmPassword.getText().toString())) {
                    password.setTextColor(Color.BLACK);
                    confirmPassword.setTextColor(Color.BLACK);
                } else {
                    password.setTextColor(Color.RED);
                    confirmPassword.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Confirm Password
        confirmPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (confirmPassword.getText().toString().length() >= 4
                        && confirmPassword.getText().toString().equals(password.getText().toString())) {
                    confirmPassword.setTextColor(Color.BLACK);
                    password.setTextColor(Color.BLACK);
                } else {
                    confirmPassword.setTextColor(Color.RED);
                    password.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        /* === Check input text condition(change text color and toast according to condition) END === */

        // EditText focus change listener (password)
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (password.getText().toString().length() < 4
                        && password.getText().toString().length() > 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "Please input more than 4 letters",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // EditText focus change listener (confirm password)
        confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (confirmPassword.getText().toString().length() < 4
                        && confirmPassword.getText().toString().length() > 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "Please input more than 4 letters",
                            Toast.LENGTH_SHORT).show();
                } else if (confirmPassword.getText().toString().length() >= 4
                        && password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(UpdateUserInfoActivity.this, "Password matched",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateUserInfoActivity.this, "Password not matched",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // When back is clicked, move to the previous activity
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /* === Check editText not null value START === */
                if (userId.getText().toString().length() == 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "Please input user id",
                            Toast.LENGTH_SHORT).show();
                    userId.requestFocus();
                    return;
                }

                if (userId.getText().toString().length() > 20) {
                    Toast.makeText(UpdateUserInfoActivity.this, "User id must be less than 20 letters",
                            Toast.LENGTH_SHORT).show();
                    userId.requestFocus();
                    return;
                }

                if (password.getText().toString().length() == 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "Please input password",
                            Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                if (confirmPassword.getText().toString().length() == 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "Please confirm password",
                            Toast.LENGTH_SHORT).show();
                    confirmPassword.requestFocus();
                    return;
                }

                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(UpdateUserInfoActivity.this, "Password not matched",
                            Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }
                /* === Check editText not null value END === */


                user = new UserVO();

                double dbHeight = height.getText().toString() == null ? 0.0 : Double.parseDouble(height.getText().toString());
                double dbWeight = weight.getText().toString() == null ? 0.0 : Double.parseDouble(weight.getText().toString());

                user.setUser_id(userId.getText().toString());
                user.setPassword(password.getText().toString());
                user.setName(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setBirthday(birthday.getText().toString());
                user.setGender(gender.getText().toString());
                user.setHeight(dbHeight);
                user.setWeight(dbWeight);
                user.setBmi(((dbWeight / dbHeight) / dbHeight) * 10000);

                /**
                 * Update user info to the database
                 */
                Call<Void> call = rs.updateUser(user);
                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(UpdateUserInfoActivity.this,
                                "Successfully updated user info.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateUserInfoActivity.this, MainHealthActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
                    }
                });

                Intent intent = new Intent(UpdateUserInfoActivity.this, MainHealthActivity.class);
                Toast.makeText(UpdateUserInfoActivity.this, "Completed updating user info.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateUserInfoActivity.this, MainHealthActivity.class);
                startActivity(intent);

            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Navigation navigation = new Navigation();
                navigation.bottomNavigation(item, UpdateUserInfoActivity.this, strUserId, txtDate);
                return true;
            }
        });

    }
}
