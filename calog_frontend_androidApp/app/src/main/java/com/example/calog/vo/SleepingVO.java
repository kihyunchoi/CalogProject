package com.example.calog.vo;

public class SleepingVO {

    private String user_id;
    private String sleeping_date;
    private int sleeping_seconds;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSleeping_date() {
        return sleeping_date;
    }

    public void setSleeping_date(String sleeping_date) {
        this.sleeping_date = sleeping_date;
    }

    public int getSleeping_seconds() {
        return sleeping_seconds;
    }

    public void setSleeping_seconds(int sleeping_seconds) {
        this.sleeping_seconds = sleeping_seconds;
    }

    @Override
    public String toString() {
        return "SleepingVO{" +
                "user_id='" + user_id + '\'' +
                ", sleeping_date=" + sleeping_date +
                ", sleeping_seconds=" + sleeping_seconds +
                '}';
    }
}
