package com.example.calog.vo;

public class FitnessMenuVO {
    private int fitness_menu_id;
    private String fitness_menu_name;
    private String fitness_menu_image;
    private double unit_calorie;

    public int getFitness_menu_id() {
        return fitness_menu_id;
    }

    public void setFitness_menu_id(int fitness_menu_id) {
        this.fitness_menu_id = fitness_menu_id;
    }

    public String getFitness_menu_name() {
        return fitness_menu_name;
    }

    public void setFitness_menu_name(String fitness_menu_name) {
        this.fitness_menu_name = fitness_menu_name;
    }

    public String getFitness_menu_image() {
        return fitness_menu_image;
    }

    public void setFitness_menu_image(String fitness_menu_image) {
        this.fitness_menu_image = fitness_menu_image;
    }

    public double getUnit_calorie() {
        return unit_calorie;
    }

    public void setUnit_calorie(double unit_calorie) {
        this.unit_calorie = unit_calorie;
    }
}
