package io.abdulmajid.near_connect.websocket.configs;

import io.abdulmajid.near_connect.websocket.services.LocationHistoryService;
import io.abdulmajid.near_connect.websocket.services.MyWebSocketHandler;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CacheManager cacheManager;

    private final LocationHistoryService locationHistoryService;

    public WebSocketConfig(CacheManager cacheManager, LocationHistoryService locationHistoryService) {
        this.cacheManager = cacheManager;
        this.locationHistoryService = locationHistoryService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(locationHistoryService, cacheManager), "/ws")
                .addInterceptors(new WebSocketHandShakeInterceptor())
                .setAllowedOriginPatterns("*");
    }


}
