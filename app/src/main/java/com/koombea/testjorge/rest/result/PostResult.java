package com.koombea.testjorge.rest.result;

import java.util.ArrayList;
import java.util.Date;

public class PostResult {
    private int id;
    private String date;
    private ArrayList<String> pics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }
}
