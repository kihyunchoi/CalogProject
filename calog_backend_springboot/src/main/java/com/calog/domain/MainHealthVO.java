package com.calog.domain;

import java.sql.Date;

public class MainHealthVO {
	private String user_id;
	private Date select_date;
	private double sum_diet_calorie;
	private double sum_fitness_calorie;
	private int sleeping_seconds;

	public MainHealthVO() {
	}

	public MainHealthVO(String user_id, Date select_date, double sum_diet_calorie, double sum_fitness_calorie,
			int sleeping_seconds) {

		this.user_id = user_id;
		this.select_date = select_date;
		this.sum_diet_calorie = sum_diet_calorie;
		this.sum_fitness_calorie = sum_fitness_calorie;
		this.sleeping_seconds = sleeping_seconds;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Date getSelect_date() {
		return select_date;
	}

	public void setSelect_date(Date select_date) {
		this.select_date = select_date;
	}

	public double getSum_diet_calorie() {
		return sum_diet_calorie;
	}

	public void setSum_diet_calorie(double sum_diet_calorie) {
		this.sum_diet_calorie = sum_diet_calorie;
	}

	public double getSum_fitness_calorie() {
		return sum_fitness_calorie;
	}

	public void setSum_fitness_calorie(double sum_fitness_calorie) {
		this.sum_fitness_calorie = sum_fitness_calorie;
	}

	public int getSleeping_seconds() {
		return sleeping_seconds;
	}

	public void setSleeping_seconds(int sleeping_seconds) {
		this.sleeping_seconds = sleeping_seconds;
	}
}
