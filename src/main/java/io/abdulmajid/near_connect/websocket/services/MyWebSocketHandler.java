package io.abdulmajid.near_connect.websocket.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.abdulmajid.near_connect.websocket.dtos.LocationDTO;
import io.abdulmajid.near_connect.websocket.dtos.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {


    private final ObjectMapperUtils objectMapperUtils;

    private final LocationCache locationCache;

    private final RabbitMQService rabbitMQService;

    private final RedisPubSub redisPubSub;

    private final ExecutorService executor;

    @Autowired
    public MyWebSocketHandler(ObjectMapperUtils objectMapperUtils, LocationCache locationCache,
                              RabbitMQService rabbitMQService, RedisPubSub redisPubSub,
                              ExecutorService executor) {

        this.objectMapperUtils = objectMapperUtils;
        this.locationCache = locationCache;
        this.rabbitMQService = rabbitMQService;
        this.redisPubSub = redisPubSub;
        this.executor = executor;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        redisPubSub.setUpChannelTopic(userId);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        log.info("Connection closed for userId: {} with status: {}", userId, status);
        locationCache.evictCache(userId);
        redisPubSub.removeChannelTopic(userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received message: {}", payload);

        try {
            Optional<LocationDTO> location  = objectMapperUtils.deserializeLocation(payload);
            session.sendMessage(new TextMessage("Echo: " + payload));

            if (location.isPresent()) {
                CompletableFuture.runAsync(() -> {
                    try {
                        LocationDTO loc = location.get();
                        locationCache.cacheLocation(loc.getUserId(), loc);
                        rabbitMQService.queueLocation(loc);
                        Optional<String> locationStr  = objectMapperUtils.serializeLocation(loc);
                        locationStr.ifPresent(locStr -> redisPubSub.publishLocation(locStr, loc.getUserId()));
                        log.info("Location processed for userId: {}", loc.getUserId());
                    } catch (Exception e) {
                        log.error("Error processing location asynchronously: ", e);
                    }
                }, executor).exceptionally(ex -> {
                    log.error("Error in async task: ", ex);
                    return null;
                });
            } else {
                log.warn("Received invalid payload: {}", payload);
                session.sendMessage(new TextMessage("Error: Invalid location data"));
            }

        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON payload: {}", payload, e);
            session.sendMessage(new TextMessage("Error: Invalid JSON format"));
        }catch (Exception e) {
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
