package com.example.calog;

import com.example.calog.vo.FitnessMenuVO;
import com.example.calog.vo.UserDietTypeDailyCalorieSumVO;
import com.example.calog.vo.DietMenuVO;
import com.example.calog.vo.DietVO;
import com.example.calog.vo.FitnessVO;
import com.example.calog.vo.MainHealthVO;
import com.example.calog.vo.SleepingVO;
import com.example.calog.vo.UserDietViewVO;
import com.example.calog.vo.UserDietTotalCaloriesViewVO;
import com.example.calog.vo.UserFitnessTotalCaloriesViewVO;
import com.example.calog.vo.UserFitnessViewVO;
import com.example.calog.vo.UserVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RemoteService {

    public static final String BASE_URL = "http://10.0.0.24:8080/calog/";

    //User
    @GET("user/list")
    Call<List<UserVO>> userList();

    @GET("user/login")
    Call<UserVO> userLogin(
            @Query("user_id") String user_id,
            @Query("password") String password);

    @GET("user/read")
    Call<UserVO> readUser(@Query("user_id") String user_id);

    @POST("user/create")
    Call<Void> createUser(@Body UserVO vo);

    @POST("user/update")
    Call<Void> updateUser(@Body UserVO vo);

    @POST("user/delete")
    Call<Void> deleteUser(@Query("user_id") String user_id);

    //User Diet Total calories
    @GET("userMainHealthData")
    Call<MainHealthVO> userMainHealthData(
            @Query("user_id") String user_id,
            @Query("select_date") String date);

    //Diet
    @GET("diet/graphDietData")
    Call<List<UserDietTotalCaloriesViewVO>> graphDietData(
            @Query("user_id") String user_id,
            @Query("start_date") String start_date,
            @Query("unit") String unit);

    @GET("diet/userDietTypeDailyCalorieSum")
    Call<List<UserDietTypeDailyCalorieSumVO>> userDietTypeDailyCalorieSum(
            @Query("user_id") String user_id,
            @Query("diet_date") String date);

    @GET("diet/userDietDailyMenuList")
    Call<List<UserDietViewVO>> userDietDailyMenuList(
            @Query("user_id") String user_id,
            @Query("diet_date") String date);

    @GET("diet/userFavoriteMenuList")
    Call<List<DietMenuVO>> userFavoriteMenuList(
            @Query("user_id") String user_id);

    @GET("diet/dietMenuList")
    Call<List<DietMenuVO>> dietMenuList(@Query("keyword") String keyword);

    @POST("diet/insertMenu")
    Call<Void> insertMenu(@Body DietVO vo);

    //Fitness
    @GET("fitness/fitnessMenuList")
    Call<List<FitnessMenuVO>> fitnessMenuList();

    @GET("fitness/graphFitnessData")
    Call<List<UserFitnessTotalCaloriesViewVO>> graphFitnessData(
            @Query("user_id") String user_id,
            @Query("start_date") String start_date,
            @Query("unit_date") String unit_date);

    @GET("fitness/userFitnessDailyTotalCalorie")
    Call<UserFitnessTotalCaloriesViewVO> userFitnessDailyTotalCalorie(
            @Query("user_id") String user_id,
            @Query("fitness_date") String fitness_date);

    @GET("fitness/userFitnessDailyMenuList")
    Call<List<UserFitnessViewVO>> userFitnessDailyMenuList(
            @Query("user_id") String user_id,
            @Query("fitness_date") String fitness_date);

    @POST("fitness/userFitnessInsert")
    Call<Void> userFitnessInsert(@Body FitnessVO vo);

    //Sleep
    @GET("sleep/graphSleepingData")
    Call<List<SleepingVO>> graphSleepingData(
            @Query("user_id") String user_id,
            @Query("start_date") String start_date,
            @Query("unit_date") String unit_date);

    @GET("sleep/userSleepingDailyInfo")
    Call<SleepingVO> userSleepingDailyInfo(
            @Query("user_id") String user_id,
            @Query("sleeping_date") String sleeping_date);

    @POST("sleep/userSleepInsert")
    Call<Void> sleepResultInsert(@Body SleepingVO vo);

}


