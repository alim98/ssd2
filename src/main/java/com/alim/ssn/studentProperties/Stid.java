package com.alim.ssn.studentProperties;

import android.app.Application;

public class Stid extends Application {
    private int stId;
    private String token;
    public int getStId() {
        return stId;
    }

    public void setStId(int stId) {
        this.stId = stId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
