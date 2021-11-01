package com.calog.domain;

import java.sql.Date;

public class UserFitnessTotalCaloriesViewVO {

	private String user_id;
	private int fitness_sum_seconds;
	private double fitness_sum_calorie_consumption;
	private Date fitness_date;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getFitness_sum_seconds() {
		return fitness_sum_seconds;
	}

	public void setFitness_sum_seconds(int fitness_sum_seconds) {
		this.fitness_sum_seconds = fitness_sum_seconds;
	}

	public double getFitness_sum_calorie_consumption() {
		return fitness_sum_calorie_consumption;
	}

	public void setFitness_sum_calorie_consumption(double fitness_sum_calorie_consumption) {
		this.fitness_sum_calorie_consumption = fitness_sum_calorie_consumption;
	}

	public Date getFitness_date() {
		return fitness_date;
	}

	public void setFitness_date(Date fitness_date) {
		this.fitness_date = fitness_date;
	}
}
