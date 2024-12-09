package io.abdulmajid.near_connect.websocket.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.abdulmajid.near_connect.websocket.dtos.LocationHistoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    private final LocationHistoryService locationHistoryService;
    private final CacheManager cacheManager;

    public MyWebSocketHandler(LocationHistoryService locationHistoryService, CacheManager cacheManager) {
        this.locationHistoryService = locationHistoryService;
        this.cacheManager = cacheManager;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        logger.info("Connection closed for userId: {} with status: {}", userId, status);
        evictCache(userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            logger.info("Received message: " + payload);
            session.sendMessage(new TextMessage("Echo: " + payload));

            // Initialize ObjectMapper and register the JavaTimeModule to handle LocalDateTime
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

            // Deserialize payload into LocationHistoryDTO
            LocationHistoryDTO locationHistoryDTO = objectMapper.readValue(payload, LocationHistoryDTO.class);
            locationHistoryService.saveLocationHistory(locationHistoryDTO);
        } catch (JsonProcessingException e) {
            logger.error("Error processing message: ", e);
            session.close(CloseStatus.SERVER_ERROR);
        } catch (Exception e) {
            logger.error("General error handling message: ", e);
            session.close(CloseStatus.SERVER_ERROR);
        }
    }



    private String getUserIdFromSession(WebSocketSession session) {
        String userId = session.getAttributes().get("userId").toString();
        logger.debug("Retrieved userId from session: {}", userId); // debug level for internal state
        return userId;
    }

    @SuppressWarnings("ConstantConditions")
    private void evictCache(String userId) {
        Cache cache = cacheManager.getCache("locationHistoryCache");
        if (cache != null) {
            logger.info("Evicting cache for userId: {}", userId);
            cache.evict(userId);
        } else {
            logger.warn("Cache not found for eviction: locationHistoryCache");
        }
    }
}
