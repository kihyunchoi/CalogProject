package com.example.calog.vo;

public class UserDietTypeDailyCalorieSumVO {
    private String user_id;
    private String diet_date;

    private String diet_type_name;
    private int sum_calorie;

    public String getDiet_type_name() {
        return diet_type_name;
    }

    public void setDiet_type_name(String diet_type_name) {
        this.diet_type_name = diet_type_name;
    }

    public int getSum_calorie() {
        return sum_calorie;
    }

    public void setSum_calorie(int sum_calorie) {
        this.sum_calorie = sum_calorie;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDiet_date() {
        return diet_date;
    }

    public void setDiet_date(String diet_date) {
        this.diet_date = diet_date;
    }
}
