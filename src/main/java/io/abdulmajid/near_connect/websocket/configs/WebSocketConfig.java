package io.abdulmajid.near_connect.websocket.configs;

import io.abdulmajid.near_connect.websocket.services.LocationCache;
import io.abdulmajid.near_connect.websocket.services.MyWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final LocationCache locationCache;

    public WebSocketConfig(LocationCache locationCache) {
        this.locationCache = locationCache;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(locationCache), "/ws")
                .addInterceptors(new WebSocketHandShakeInterceptor())
                .setAllowedOriginPatterns("*");
    }


}
