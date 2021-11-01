package com.example.calog.vo;

public class UserDietTotalCaloriesViewVO {

    private String user_id;
    private double sum_calorie;
    private String diet_date;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getSum_calorie() {
        return sum_calorie;
    }

    public void setSum_calorie(double sum_calorie) {
        this.sum_calorie = sum_calorie;
    }

    public String getDiet_date() {
        return diet_date;
    }

    public void setDiet_date(String diet_date) {
        this.diet_date = diet_date;
    }

    @Override
    public String toString() {
        return "UserTotalCaloriesViewVO{" +
                "user_id='" + user_id + '\'' +
                ", sum_calorie=" + sum_calorie +
                ", diet_date=" + diet_date +
                '}';
    }
}
