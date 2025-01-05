package io.abdulmajid.near_connect.websocket.configs;

import io.abdulmajid.near_connect.websocket.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {

    private final UserContext userContext;

    public WebSocketHandShakeInterceptor(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        URI uri = request.getURI();
        String query = uri.getQuery();

        log.debug("Handshake URI: {}", uri);
        log.debug("Query String: {}", query);

        if (query != null && query.contains("userId")) {
            // Parse the query string for userId
            String userId = getQueryParam(query, "userId");
            if (userId != null) {
                attributes.put("userId", userId);
                userContext.setUserId(userId);
                log.info("Handshake initiated. User-ID from query parameter: {}", userId);
            } else {
                log.warn("User-ID not found in query parameters.");
            }
        } else {
            log.warn("Query parameters are missing or userId is not included.");
        }
        return true;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("Error during WebSocket handshake: {}", exception.getMessage());
        } else {
            log.info("Handshake completed successfully.");
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
