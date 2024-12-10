package io.abdulmajid.near_connect.websocket.services;

import io.abdulmajid.near_connect.websocket.dtos.LocationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LocationWorker {
    private final LocationService locationService;

    public LocationWorker(LocationService locationService) {
        this.locationService = locationService;
    }


    @RabbitListener(queues = "#{T(io.abdulmajid.near_connect.websocket.services.RabbitMQQueue).LOCATION_UPDATES.getQueueName()}")
    public void consumeLocationUpdate(LocationDTO location) {
        log.info("Received location update from RabbitMQ: {}", location);

        try {
            LocationDTO savedLocation = locationService.saveLocationHistory(location);
            log.info("Saved location update to database: {}", savedLocation);
        } catch (Exception e) {
            log.error("Failed to save location update to database", e);
        }
    }
}
