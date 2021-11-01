package com.example.calog.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.join.MainJoinActivity;
import com.example.calog.join.UpdateUserInfoActivity;
import com.example.calog.vo.UserVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class Membership {

    boolean logInStatus;

    /**
     * Check SignIn status and set visibility
     *
     * @param menu
     * @param logInStatus
     * @return menu
     */
    public Menu SignInStatus(Menu menu, boolean logInStatus) {
        if (logInStatus) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
        } else {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }
        return menu;
    }

    /**
     * Check membership status whether a user want to signIn, signOut, update info, or delete membership
     *
     * @param item
     * @param activity
     * @param pref
     * @param strUserId
     * @param userId
     * @return menuItem
     */
    public MenuItem membershipStatus(MenuItem item, Activity activity, SharedPreferences pref,
                                     String strUserId, TextView userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RemoteService rs = retrofit.create(RemoteService.class);

        Intent intent;

        switch (item.getItemId()) {
            case R.id.signIn:
                intent = new Intent(activity, MainJoinActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.signOut:
                Toast.makeText(activity, "Successfully signed out.", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_id", "");
                editor.commit();
                userId.setText("");
                logInStatus = false;
                intent = new Intent(activity, MainJoinActivity.class);
                activity.startActivity(intent);
                activity.finish();
                break;

            case R.id.update:
                Call<UserVO> call = rs.readUser(strUserId);
                call.enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                        UserVO user = response.body();

                        Intent intent = new Intent(activity, UpdateUserInfoActivity.class);

                        intent.putExtra("user_id", user.getUser_id());
                        intent.putExtra("password", user.getPassword());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("name", user.getName());
                        intent.putExtra("phone", user.getPhone());
                        intent.putExtra("birthday", user.getBirthday());
                        intent.putExtra("gender", user.getGender());
                        intent.putExtra("height", user.getHeight());
                        intent.putExtra("weight", user.getWeight());
                        intent.putExtra("bmi", user.getBmi());

                        activity.startActivity(intent);
                        activity.finish();
                    }

                    @Override
                    public void onFailure(Call<UserVO> call, Throwable t) {
                        System.out.println("=====" + activity.getLocalClassName()
                                + " readUser onFailure: " + t.toString());
                    }
                });
                break;

            case R.id.withdrawal:
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Membership Withdrawal");
                builder.setMessage("Are you sure you want to withdraw membership?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = rs.deleteUser(strUserId);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(activity,
                                        "Completed membership withdrawal.", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", "");
                                editor.commit();
                                userId.setText("");
                                logInStatus = false;

                                Intent intent = new Intent(activity, MainJoinActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                System.out.println("=====" + activity.getLocalClassName()
                                        + " deleteUser onFailure: " + t.toString());
                            }
                        });
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(activity,
                                "Canceled membership withdrawal.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
        }
        return item;
    }
}
