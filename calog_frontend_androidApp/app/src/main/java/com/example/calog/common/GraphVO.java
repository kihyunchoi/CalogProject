package com.example.calog.common;

public class GraphVO {

    float dataFloat;
    String dataDate;

    public GraphVO() {
    }

    public GraphVO(float dataFloat, String dataDate) {
        this.dataFloat = dataFloat;
        this.dataDate = dataDate;
    }

    public float getDataFloat() {
        return dataFloat;
    }

    public void setDataFloat(float dataFloat) {
        this.dataFloat = dataFloat;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }
}
