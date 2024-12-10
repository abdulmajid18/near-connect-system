package io.abdulmajid.near_connect.websocket.services;


public enum RabbitMQQueue {
    LOCATION_UPDATES("locationUpdates");

    private final String queueName;

    RabbitMQQueue(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }
}
