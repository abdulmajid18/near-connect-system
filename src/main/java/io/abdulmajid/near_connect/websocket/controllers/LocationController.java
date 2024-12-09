package io.abdulmajid.near_connect.websocket.controllers;


import io.abdulmajid.near_connect.websocket.dtos.LocationHistoryDTO;
import io.abdulmajid.near_connect.websocket.services.LocationHistoryService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class LocationController {

    private final LocationHistoryService locationHistoryService;

    public LocationController(LocationHistoryService locationHistoryService) {
        this.locationHistoryService = locationHistoryService;
    }


    @MessageMapping("/location")
    public void handleLocationUpdate(@Payload LocationHistoryDTO message) {
        System.out.println("Received location: " + message);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + message.getLongitude());
        LocationHistoryDTO savedMessage = locationHistoryService.saveLocationHistory(message);
        System.out.println("Location Saved: " + savedMessage );

    }
}
