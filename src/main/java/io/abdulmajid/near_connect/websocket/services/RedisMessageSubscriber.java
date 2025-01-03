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
        String fromChannel = new String(message.getChannel());
        String toChannelPattern = new String(pattern);
        String content = new String(message.getBody());

        log.info("Message Received: \nFrom Channel: {} \nSubscribed Pattern: {} \nContent: {}",
                fromChannel, toChannelPattern, content);
    }

    public void owner(Message message, byte[] pattern) {
        String fromChannel = new String(message.getChannel());
        String toChannelPattern = new String(pattern);
        String content = new String(message.getBody());

        log.info("Owner Message Received: \nFrom Channel: {} \nSubscribed Pattern: {} \nContent: {}",
                fromChannel, toChannelPattern, content);
    }


}