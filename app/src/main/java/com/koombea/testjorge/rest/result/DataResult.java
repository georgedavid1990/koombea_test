package com.koombea.testjorge.rest.result;


import java.util.List;

public class DataResult {
    private List<UserPostResult> data;

    public void setData(List<UserPostResult> data) {
        this.data = data;
    }

    public List<UserPostResult> getData() {
        return data;
    }
}
