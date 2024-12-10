package io.abdulmajid.near_connect.websocket.services;

import io.abdulmajid.near_connect.websocket.dtos.LocationDTO;
import io.abdulmajid.near_connect.websocket.dtos.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {


    private final LocationCache locationCache;

    private final RabbitTemplate rabbitTemplate;

    public MyWebSocketHandler(LocationCache locationCache, RabbitTemplate rabbitTemplate) {
        this.locationCache = locationCache;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        log.info("Connection closed for userId: {} with status: {}", userId, status);
        locationCache.evictCache(userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            log.info("Received message: " + payload);
            session.sendMessage(new TextMessage("Echo: " + payload));

            LocationDTO location  = ObjectMapperUtils.deserializeLocation(payload);
            if (location != null){
                locationCache.cacheLocation(location.getUserId(), location);
                rabbitTemplate.convertAndSend(RabbitMQQueue.LOCATION_UPDATES.getQueueName(), location);
            }

        } catch (Exception e) {
            log.error("General error handling message: ", e);
            session.close(CloseStatus.SERVER_ERROR);
        }
    }



    private String getUserIdFromSession(WebSocketSession session) {
        String userId = session.getAttributes().get("userId").toString();
        log.debug("Retrieved userId from session: {}", userId); // debug level for internal state
        return userId;
    }

}
