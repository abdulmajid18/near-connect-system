package io.abdulmajid.near_connect.websocket.configs;

import io.abdulmajid.near_connect.websocket.services.LocationCache;
import io.abdulmajid.near_connect.websocket.services.MyWebSocketHandler;
import io.abdulmajid.near_connect.websocket.services.RedisPubSub;
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

    private final RedisPubSub redisPubSub;

    public WebSocketConfig(LocationCache locationCache, RabbitTemplate rabbitTemplate, RedisPubSub redisPubSub) {
        this.locationCache = locationCache;
        this.rabbitTemplate = rabbitTemplate;
        this.redisPubSub = redisPubSub;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(locationCache, rabbitTemplate, redisPubSub), "/ws")
                .addInterceptors(new WebSocketHandShakeInterceptor())
                .setAllowedOriginPatterns("*");
    }


}
