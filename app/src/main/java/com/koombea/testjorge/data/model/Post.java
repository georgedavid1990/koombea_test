package com.koombea.testjorge.data.model;

import java.util.ArrayList;
import java.util.Date;

public class Post {

    private int id;
    private Date date;
    private ArrayList<String> pics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

}
