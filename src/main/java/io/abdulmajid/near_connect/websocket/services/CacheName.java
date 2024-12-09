package io.abdulmajid.near_connect.websocket.services;

public enum CacheName {
    LOCATION_CACHE("locationCache");

    private final String cacheName;

    CacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCacheName() {
        return cacheName;
    }
}
