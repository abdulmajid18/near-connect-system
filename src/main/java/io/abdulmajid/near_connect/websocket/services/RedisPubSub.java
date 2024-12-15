package io.abdulmajid.near_connect.websocket.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisPubSub {
    private final StringRedisTemplate redisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final MessageListenerAdapter listenerAdapter;

    public RedisPubSub(StringRedisTemplate redisTemplate, RedisMessageListenerContainer redisMessageListenerContainer,
                       MessageListenerAdapter listenerAdapter) {
        this.redisTemplate = redisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.listenerAdapter = listenerAdapter;
    }

    // Set up dynamic Pub/Sub for the user
    public void setUpChannelTopic(String userId) {
        String topicName = "user:" + userId + ":location";
        ChannelTopic topic = new ChannelTopic(topicName);

        redisMessageListenerContainer.addMessageListener(listenerAdapter, topic);
        log.info("Subscribed to topic: {}", topicName);
    }

    // Remove dynamic Pub/Sub for the user
    public void removeChannelTopic(String userId) {
        String topicName = "user:" + userId + ":location";
        ChannelTopic topic = new ChannelTopic(topicName);

        // Remove the message listener from the container for the user topic
        redisMessageListenerContainer.removeMessageListener(listenerAdapter, topic);
        log.info("Unsubscribed from topic: {}", topicName);
    }

    public void publishLocation(String location, String userId) {
        String topicName = "user:" + userId + ":location";
        redisTemplate.convertAndSend(topicName, location);
        log.info("Message published: {}", location);
    }
}

