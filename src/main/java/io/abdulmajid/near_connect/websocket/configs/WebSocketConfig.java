package io.abdulmajid.near_connect.websocket.configs;

import io.abdulmajid.near_connect.websocket.services.LocationCache;
import io.abdulmajid.near_connect.websocket.services.MyWebSocketHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final LocationCache locationCache;

    private final RabbitTemplate rabbitTemplate;

    public WebSocketConfig(LocationCache locationCache, RabbitTemplate rabbitTemplate) {
        this.locationCache = locationCache;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(locationCache, rabbitTemplate), "/ws")
                .addInterceptors(new WebSocketHandShakeInterceptor())
                .setAllowedOriginPatterns("*");
    }


}
