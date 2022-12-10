package com.example.ocenknajpce;

import java.io.Serializable;

public class Restaurant implements Serializable {
    public Restaurant(int id, String rName, String rPhone, double rLat, double rLong) {
        this.resId = id;
        this.resName = rName;
        this.resPhone = rPhone;
        this.resLat = rLat;
        this.resLong = rLong;
    }

    public String getResName() {
        return this.resName;
    }

    public String getResPhone() {
        return this.resPhone;
    }

    public double getLat() {
        return this.resLat;
    }

    public double getLong() {
        return this.resLong;
    }

    int resId;
    String resName, resPhone;
    double  resLat, resLong;
}
