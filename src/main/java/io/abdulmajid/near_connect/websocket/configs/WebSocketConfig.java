package io.abdulmajid.near_connect.websocket.configs;

import io.abdulmajid.near_connect.websocket.services.MyWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final MyWebSocketHandler myWebSocketHandler;

    private final WebSocketHandShakeInterceptor webSocketHandShakeInterceptor;

    public WebSocketConfig(MyWebSocketHandler myWebSocketHandler, WebSocketHandShakeInterceptor webSocketHandShakeInterceptor) {
        this.myWebSocketHandler = myWebSocketHandler;
        this.webSocketHandShakeInterceptor = webSocketHandShakeInterceptor;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/ws")
                .addInterceptors(webSocketHandShakeInterceptor)
                .setAllowedOriginPatterns("*");
    }


}
