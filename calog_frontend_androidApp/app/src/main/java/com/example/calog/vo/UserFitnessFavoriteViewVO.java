package com.example.calog.vo;

public class UserFitnessFavoriteViewVO {

    private String user_id;
    private String fitness_type_name;
    private String fitness_menu_name;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFitness_type_name() {
        return fitness_type_name;
    }

    public void setFitness_type_name(String fitness_type_name) {
        this.fitness_type_name = fitness_type_name;
    }

    public String getFitness_menu_name() {
        return fitness_menu_name;
    }

    public void setFitness_menu_name(String fitness_menu_name) {
        this.fitness_menu_name = fitness_menu_name;
    }

    @Override
    public String toString() {
        return "UserFitnessFavoriteViewVO{" +
                "user_id='" + user_id + '\'' +
                ", fitness_type_name='" + fitness_type_name + '\'' +
                ", fitness_menu_name='" + fitness_menu_name + '\'' +
                '}';
    }
}
