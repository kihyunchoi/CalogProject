package com.example.calog.join;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.vo.UserVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;


public class SignupActivity extends AppCompatActivity {

    RemoteService rs;
    UserVO user;
    Retrofit retrofit;

    //Button btnSave, btnCancel
    Button btnSave, btnCancel;

    //ImageView back, home
    ImageView back;

    EditText userId, password, confirmPassword, email, name,
            phone, gender, birthday, height, weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Change color status bar
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        // RemoteServcie, Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        userId = (EditText) findViewById(R.id.user_id);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        birthday = findViewById(R.id.birthday);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        gender = (EditText) findViewById(R.id.gender);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        /* === Check Text condition(change text color and toast according to condition) START === */
        //User id
        userId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userId.getText().toString().trim().length() <= 20) {
                    userId.setTextColor(Color.BLACK);
                } else {
                    userId.setTextColor(Color.RED);
                    Toast.makeText(SignupActivity.this, "Please input user id",
                            Toast.LENGTH_LONG).show();
                    userId.requestFocus();
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
                    Toast.makeText(SignupActivity.this, "Please input more than 4 letters",
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
                    Toast.makeText(SignupActivity.this, "Please input more than 4 letters",
                            Toast.LENGTH_SHORT).show();
                } else if (confirmPassword.getText().toString().length() >= 4
                        && password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(SignupActivity.this, "Password matched",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Password not matched",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // When back is clicked, move to the previous activity
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(SignupActivity.this, "Please input user id",
                            Toast.LENGTH_SHORT).show();
                    userId.requestFocus();
                    return;
                }

                if (userId.getText().toString().length() > 20) {
                    Toast.makeText(SignupActivity.this, "User id must be less than 20 letters",
                            Toast.LENGTH_SHORT).show();
                    userId.requestFocus();
                    return;
                }

                if (password.getText().toString().length() == 0) {
                    Toast.makeText(SignupActivity.this, "Please input password",
                            Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                if (confirmPassword.getText().toString().length() == 0) {
                    Toast.makeText(SignupActivity.this, "Please confirm password",
                            Toast.LENGTH_SHORT).show();
                    confirmPassword.requestFocus();
                    return;
                }

                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(SignupActivity.this, "Password not matched",
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
                 * Create a user and insert user info to the database
                 */
                Call<Void> call = rs.createUser(user);
                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(SignupActivity.this, "Saved.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainJoinActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

                // When the btnSave clicked, move to the Main join activity
                Intent intent = new Intent(SignupActivity.this, MainJoinActivity.class);
                Toast.makeText(SignupActivity.this, "Completed Sign up.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // When the btnCancel clicked, move to the previous activity
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
