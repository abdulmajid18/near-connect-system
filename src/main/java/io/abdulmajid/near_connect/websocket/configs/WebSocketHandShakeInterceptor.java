package io.abdulmajid.near_connect.websocket.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandShakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        URI uri = request.getURI();
        String query = uri.getQuery();

        logger.debug("Handshake URI: {}", uri);
        logger.debug("Query String: {}", query);

        if (query != null && query.contains("userId")) {
            // Parse the query string for userId
            String userId = getQueryParam(query, "userId");
            if (userId != null) {
                attributes.put("userId", userId);
                logger.info("Handshake initiated. User-ID from query parameter: {}", userId);
            } else {
                logger.warn("User-ID not found in query parameters.");
            }
        } else {
            logger.warn("Query parameters are missing or userId is not included.");
        }
        return true;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            logger.error("Error during WebSocket handshake: {}", exception.getMessage());
        } else {
            logger.info("Handshake completed successfully.");
        }
    }

    /**
     * Utility method to extract a specific query parameter from a query string.
     *
     * @param query The full query string (e.g., "key1=value1&key2=value2").
     * @param param The parameter to extract (e.g., "userId").
     * @return The value of the parameter, or null if not found.
     */
    private String getQueryParam(String query, String param) {
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(param)) {
                return keyValue[1];
            }
        }
        return null;
    }
}
