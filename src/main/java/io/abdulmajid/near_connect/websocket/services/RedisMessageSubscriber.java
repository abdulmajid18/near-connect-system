package io.abdulmajid.near_connect.websocket.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Extract the channel name and message content
        String fromChannel = new String(message.getChannel());
        String content = new String(message.getBody());

        // Log only the channel name and message content
        log.info("Message Received: \nChannel: {} \nContent: {}", fromChannel, content);
    }

}