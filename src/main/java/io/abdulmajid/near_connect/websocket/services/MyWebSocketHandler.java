package io.abdulmajid.near_connect.websocket.services;

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
    private final CacheManager cacheManager;

    public MyWebSocketHandler(CacheManager cacheManager) {
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
            System.out.println("Received message: " + message.getPayload());
            // Simulate echo response for testing
            session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
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
