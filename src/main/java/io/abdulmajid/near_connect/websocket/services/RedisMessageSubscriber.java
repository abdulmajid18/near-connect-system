package io.abdulmajid.near_connect.websocket.services;

import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber {
    public void onMessage(String message, String channel) {
        System.out.println("Received message: " + message + " on channel: " + channel);
    }
}