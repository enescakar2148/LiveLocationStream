package com.enescakar.istexprs.Model;

public class Locations {
    private String latitude;
    private String longitude;

    public Locations(){}


    public Locations(Double latitude, Double longitude) {
        this.latitude = latitude.toString();
        this.longitude = longitude.toString();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}