package com.example.calog.common;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.fitness.SearchFitnessActivity;
import com.example.calog.sleeping.SleepCheckActivity;

public class Navigation {
    Intent intent;

    /**
     * Bottom Navigation menu
     *
     * @param item
     * @param activity
     * @param strUserId
     * @param txtDate
     */
    public void bottomNavigation(MenuItem item, Activity activity, String strUserId, TextView txtDate) {

        switch (item.getItemId()) {
            case R.id.fitnessStart: {
                intent = new Intent(activity, SearchFitnessActivity.class);

                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", txtDate.getText().toString());

                activity.startActivity(intent);
                break;
            }
            case R.id.homeMenu: {
                intent = new Intent(activity, MainHealthActivity.class);

                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", txtDate.getText().toString());

                activity.startActivity(intent);
                break;
            }
            case R.id.sleepMenu: {
                intent = new Intent(activity, SleepCheckActivity.class);

                intent.putExtra("user_id", strUserId);
                intent.putExtra("select_date", txtDate.getText().toString());

                activity.startActivity(intent);
                break;
            }
        }
    }
}
