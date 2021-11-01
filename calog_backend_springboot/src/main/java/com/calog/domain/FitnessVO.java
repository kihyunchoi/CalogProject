package com.calog.domain;

import java.sql.Date;

public class FitnessVO {

	private int fitness_id;
	private String user_id;
	private Date fitness_date;
	private int fitness_menu_id;
	private int fitness_seconds;
	private double calorie_consumption;

	public int getFitness_id() {
		return fitness_id;
	}

	public void setFitness_id(int fitness_id) {
		this.fitness_id = fitness_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Date getFitness_date() {
		return fitness_date;
	}

	public void setFitness_date(Date fitness_date) {
		this.fitness_date = fitness_date;
	}

	public int getFitness_menu_id() {
		return fitness_menu_id;
	}

	public void setFitness_menu_id(int fitness_menu_id) {
		this.fitness_menu_id = fitness_menu_id;
	}

	public int getFitness_seconds() {
		return fitness_seconds;
	}

	public void setFitness_seconds(int fitness_seconds) {
		this.fitness_seconds = fitness_seconds;
	}

	public double getCalorie_consumption() {
		return calorie_consumption;
	}

	public void setCalorie_consumption(double calorie_consumption) {
		this.calorie_consumption = calorie_consumption;
	}

}
