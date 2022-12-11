package com.example.ocenknajpce;

import java.io.Serializable;

public class Restaurant implements Serializable {
    public Restaurant(String id, String rName, String rPhone, double rLat, double rLong) {
        this.resId = id;
        this.resName = rName;
        this.resPhone = rPhone;
        this.resLat = rLat;
        this.resLong = rLong;
    }

    public String getResId() {
        return this.resId;
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

    String resId, resName, resPhone;
    double resLat, resLong;
}
