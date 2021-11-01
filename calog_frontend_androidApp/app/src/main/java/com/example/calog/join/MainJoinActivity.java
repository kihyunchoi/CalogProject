package com.example.calog.join;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.vo.UserVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class MainJoinActivity extends AppCompatActivity {

    EditText userId, password;
    Retrofit retrofit;
    RemoteService rs;
    UserVO user;

    Button btnLogin, btnJoin;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_join);

        //Change status Bar color
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        userId = findViewById(R.id.user_id);
        password = findViewById(R.id.password);

        // RemoteServcie, Retrofit
        retrofit = new Retrofit.Builder()                           // Create Retrofit builder
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);              // Create API interface

        //SignIn button
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<UserVO> call = rs.userLogin(userId.getText().toString(), password.getText().toString());
                call.enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {

                        user = response.body();
                        System.out.println(user.getUser_id());

                        //Save userid to SharedPreference
                        SharedPreferences pref = getSharedPreferences("pjLogin", 0);

                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("user_id", user.getUser_id().toString());
                        edit.commit();

                        Toast.makeText(MainJoinActivity.this, user.getUser_id().toString(), Toast.LENGTH_SHORT).show();

                        intent = new Intent(MainJoinActivity.this, MainHealthActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<UserVO> call, Throwable t) {
                        Toast.makeText(MainJoinActivity.this, "Please input user id and password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Signup button
        btnJoin = findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainJoinActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
