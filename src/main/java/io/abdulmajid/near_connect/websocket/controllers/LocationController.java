package io.abdulmajid.near_connect.websocket.controllers;


import io.abdulmajid.near_connect.websocket.dtos.LocationMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LocationController {

    private final SimpMessagingTemplate messagingTemplate;

    public LocationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/location")
    public void handleLocationUpdate(LocationMessage message) {
        System.out.println("Received location: " + message);
    }
}