package com.example.calog.diet;

import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.common.GraphFragment;
import com.example.calog.common.GraphPagerFragment;
import com.example.calog.common.GraphVO;
import com.example.calog.vo.UserDietTotalCaloriesViewVO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;
import static com.example.calog.diet.DietActivity.selectedDate;
import static com.example.calog.diet.DietActivity.strUserId;

public class DietGraph extends AsyncTask<Integer, Integer, ArrayList<GraphFragment>> {
    ArrayList<GraphVO> daySumList = new ArrayList<>();
    ArrayList<GraphVO> weekSumList = new ArrayList<>();
    ArrayList<GraphVO> monthSumList = new ArrayList<>();

    List<UserDietTotalCaloriesViewVO> userDietTotalCalorieList = new ArrayList<>();

    Retrofit retrofit;
    RemoteService rs;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        //Get a date monday of week given selected date
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.valueOf(selectedDate));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String monday = formatter.format(calendar.getTime());

        /**
         * Show week graph
         */
        Call<List<UserDietTotalCaloriesViewVO>> call = rs.graphDietData(strUserId, monday, "week");
        call.enqueue(new Callback<List<UserDietTotalCaloriesViewVO>>() {

            @Override
            public void onResponse(Call<List<UserDietTotalCaloriesViewVO>> call, Response<List<UserDietTotalCaloriesViewVO>> response) {
                userDietTotalCalorieList = response.body();

                for (int i = 0; i < userDietTotalCalorieList.size(); i++) {
                    UserDietTotalCaloriesViewVO vo = userDietTotalCalorieList.get(i);

                    daySumList.add(new GraphVO((float) vo.getSum_calorie(), vo.getDiet_date())); //날짜중 년도를 짤라냄
                }

                GraphFragment.sumCalorieListWeek = new ArrayList<>();

                if (daySumList.size() != 0) {
                    GraphFragment.sumCalorieListWeek = daySumList;
                }
            }

            @Override
            public void onFailure(Call<List<UserDietTotalCaloriesViewVO>> call, Throwable t) {
                System.out.println("=====DietGraph weekTotalCalorie onFailure: " + t.toString());
            }
        });

        //Get first day of month given selected date
        calendar.setTime(Date.valueOf(selectedDate));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String monthFirstDay = formatter.format(calendar.getTime());

        /**
         * Show month graph
         */
        Call<List<UserDietTotalCaloriesViewVO>> callMonth = rs.graphDietData(strUserId, monthFirstDay, "month");
        callMonth.enqueue(new Callback<List<UserDietTotalCaloriesViewVO>>() {

            @Override
            public void onResponse(Call<List<UserDietTotalCaloriesViewVO>> call, Response<List<UserDietTotalCaloriesViewVO>> response) {
                userDietTotalCalorieList = response.body();

                for (int i = 0; i < userDietTotalCalorieList.size(); i++) {
                    UserDietTotalCaloriesViewVO vo = userDietTotalCalorieList.get(i);

                    weekSumList.add(new GraphVO((float) vo.getSum_calorie(), vo.getDiet_date()));
                }

                GraphFragment.sumCalorieListMonth = new ArrayList<>();

                if (weekSumList.size() != 0) {
                    GraphFragment.sumCalorieListMonth = weekSumList;
                }
            }

            @Override
            public void onFailure(Call<List<UserDietTotalCaloriesViewVO>> call, Throwable t) {
                System.out.println("=====DietGraph monthTotalCalorie onFailure: " + t.toString());
            }
        });


        /**
         * Show year graph
         */
        Call<List<UserDietTotalCaloriesViewVO>> callYear = rs.graphDietData(strUserId, monthFirstDay, "year");
        callYear.enqueue(new Callback<List<UserDietTotalCaloriesViewVO>>() {
            @Override
            public void onResponse(Call<List<UserDietTotalCaloriesViewVO>> call,
                                   Response<List<UserDietTotalCaloriesViewVO>> response) {
                userDietTotalCalorieList = response.body();

                for (int i = 0; i < userDietTotalCalorieList.size(); i++) {
                    UserDietTotalCaloriesViewVO vo = userDietTotalCalorieList.get(i);

                    String date = vo.getDiet_date().substring(5);
                    date = date.substring(0, 2);

                    monthSumList.add(new GraphVO((float) vo.getSum_calorie(), date));
                }

                float sum = 0f;
                int div = 0;

                ArrayList<GraphVO> monthSumListRes = new ArrayList<>();

                String currentMonth = "";
                if (monthSumList.size() != 0) {
                    currentMonth = monthSumList.get(0).getDataDate();
                }

                for (int i = 0; i < monthSumList.size(); i++) {
                    if (!currentMonth.equals(monthSumList.get(i).getDataDate())
                            || monthSumList.size() - 1 == i) {

                        if (monthSumList.size() - 1 == i) {
                            sum += monthSumList.get(i).getDataFloat();
                            div++;
                        }

                        float avg = sum / div;

                        monthSumListRes.add(new GraphVO(avg, currentMonth));

                        sum = monthSumList.get(i).getDataFloat();
                        div = 1;

                        currentMonth = monthSumList.get(i).getDataDate();
                        continue;
                    }

                    sum += monthSumList.get(i).getDataFloat();
                    div++;
                }

                GraphFragment.sumCalorieListYear = new ArrayList<>();

                if (monthSumListRes.size() != 0) {
                    GraphFragment.sumCalorieListYear = monthSumListRes;
                }
            }

            @Override
            public void onFailure(Call<List<UserDietTotalCaloriesViewVO>> call, Throwable t) {
                System.out.println("=====DietGraph yearTotalCalorie onFailure" + t.toString());
            }
        });

    }

    @Override
    protected ArrayList<GraphFragment> doInBackground(Integer... integers) {

        while (true) {
            if (monthSumList.size() != 0) {
                break;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<GraphFragment> graphFragments) {
        super.onPostExecute(graphFragments);

        FragmentManager fm = new FragmentManager() {
        };
        FragmentTransaction tr = fm.beginTransaction();

        GraphPagerFragment graphFragment = new GraphPagerFragment();
        tr.replace(R.id.barChartFrag, graphFragment);

    }
}

