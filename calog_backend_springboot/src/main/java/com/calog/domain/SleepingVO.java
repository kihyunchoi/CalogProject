package com.calog.domain;

import java.sql.Date;

public class SleepingVO {

	private String user_id;
	private Date sleeping_date;
	private int sleeping_seconds;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Date getSleeping_date() {
		return sleeping_date;
	}

	public void setSleeping_date(Date sleeping_date) {
		this.sleeping_date = sleeping_date;
	}

	public int getSleeping_seconds() {
		return sleeping_seconds;
	}

	public void setSleeping_seconds(int sleeping_seconds) {
		this.sleeping_seconds = sleeping_seconds;
	}

}
