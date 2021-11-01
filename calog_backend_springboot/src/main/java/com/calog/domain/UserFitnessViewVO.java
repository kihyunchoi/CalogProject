package com.calog.domain;

import java.sql.Date;

public class UserFitnessViewVO {

	private String user_id;
	private String fitness_menu_name;
	private int fitness_seconds;
	private double unit_calorie;
	private double calorie_consumption;
	private Date fitness_date;
	private String fitness_menu_image;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFitness_menu_name() {
		return fitness_menu_name;
	}

	public void setFitness_menu_name(String fitness_menu_name) {
		this.fitness_menu_name = fitness_menu_name;
	}

	public int getFitness_seconds() {
		return fitness_seconds;
	}

	public void setFitness_seconds(int fitness_seconds) {
		this.fitness_seconds = fitness_seconds;
	}

	public double getUnit_calorie() {
		return unit_calorie;
	}

	public void setUnit_calorie(double unit_calorie) {
		this.unit_calorie = unit_calorie;
	}

	public double getCalorie_consumption() {
		return calorie_consumption;
	}

	public void setCalorie_consumption(double calorie_consumption) {
		this.calorie_consumption = calorie_consumption;
	}

	public Date getFitness_date() {
		return fitness_date;
	}

	public void setFitness_date(Date fitness_date) {
		this.fitness_date = fitness_date;
	}

	public String getFitness_menu_image() {
		return fitness_menu_image;
	}

	public void setFitness_menu_image(String fitness_menu_image) {
		this.fitness_menu_image = fitness_menu_image;
	}
}
