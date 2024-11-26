package io.abdulmajid.near_connect.websocket.dtos;

public class LocationMessage {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location Message {" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
