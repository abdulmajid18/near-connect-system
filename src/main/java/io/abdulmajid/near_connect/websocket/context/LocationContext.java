package io.abdulmajid.near_connect.websocket.context;

import io.abdulmajid.near_connect.websocket.dtos.LocationDTO;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LocationContext {
    private LocationDTO currentLocation;

    // Get the current location
    public LocationDTO getCurrentLocation() {
        if (currentLocation == null) {
            log.warn("Current location is not set.");
        }
        return currentLocation;
    }

    // Set the current location
    public void setCurrentLocation(LocationDTO location) {
        if (location != null) {
            this.currentLocation = location;
            log.info("Current location set: {}", location);
        } else {
            log.warn("Attempted to set null location.");
        }
    }

    // Update the current location
    public void updateLocation(LocationDTO location) {
        if (location != null) {
            this.currentLocation = location;
            log.info("Current location updated: {}", location);
        } else {
            log.warn("Attempted to update to a null location.");
        }
    }

    // Clear the current location (optional)
    public void clearLocation() {
        log.info("Clearing current location.");
        this.currentLocation = null;
    }

    // Check if location is set
    public boolean isLocationSet() {
        return currentLocation != null;
    }
}
