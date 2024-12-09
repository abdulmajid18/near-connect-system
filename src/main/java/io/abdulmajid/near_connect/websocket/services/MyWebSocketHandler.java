package io.abdulmajid.near_connect.websocket.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.abdulmajid.near_connect.websocket.dtos.LocationHistoryDTO;
import io.abdulmajid.near_connect.websocket.dtos.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    private final LocationCache locationCache;

    public MyWebSocketHandler(LocationCache locationCache) {
        this.locationCache = locationCache;
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        logger.info("Connection closed for userId: {} with status: {}", userId, status);
        locationCache.evictCache(userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            logger.info("Received message: " + payload);
            session.sendMessage(new TextMessage("Echo: " + payload));

            LocationHistoryDTO location  = ObjectMapperUtils.deserializeLocation(payload);
            if (location != null){
                locationCache.cacheLocation(location.getUserId(), location);
            }

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

}
