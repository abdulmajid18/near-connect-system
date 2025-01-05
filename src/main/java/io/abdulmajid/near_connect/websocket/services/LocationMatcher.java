package io.abdulmajid.near_connect.websocket.services;

import io.abdulmajid.near_connect.websocket.dtos.LocationDTO;

public class LocationMatcher {

    public static double radius(LocationDTO myLocation, LocationDTO friendsLocation) {
        // Extract latitude and longitude from the LocationDTO objects
        double lat1 = myLocation.getLatitude();
        double lon1 = myLocation.getLongitude();
        double lat2 = friendsLocation.getLatitude();
        double lon2 = friendsLocation.getLongitude();

        // Haversine formula implementation
        double R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}

