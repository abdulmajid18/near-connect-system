package io.abdulmajid.near_connect.websocket.controllers;


import io.abdulmajid.near_connect.websocket.dtos.LocationDTO;
import io.abdulmajid.near_connect.websocket.services.LocationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @MessageMapping("/location")
    public void handleLocationUpdate(@Payload LocationDTO message) {
        System.out.println("Received location: " + message);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + message.getLongitude());
        LocationDTO savedMessage = locationService.saveLocationHistory(message);
        System.out.println("Location Saved: " + savedMessage );

    }
}
