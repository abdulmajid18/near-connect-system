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
        String topicName = generateTopicName(userId);
        ChannelTopic topic = new ChannelTopic(topicName);

        redisMessageListenerContainer.addMessageListener(listenerAdapter, topic);
        log.info("Subscribed to topic: {}", topicName);
    }

    // Remove dynamic Pub/Sub for the user
    public void removeChannelTopic(String userId) {
        String topicName = generateTopicName(userId);
        ChannelTopic topic = new ChannelTopic(topicName);

        // Remove the message listener from the container for the user topic
        redisMessageListenerContainer.removeMessageListener(listenerAdapter, topic);
        log.info("Unsubscribed from topic: {}", topicName);
    }

    private String generateTopicName(String userId) {
        return "user:" + userId + ":location";
    }

    public void publishLocation(String location, String userId) {
        String topicName = generateTopicName(userId);
        try {
            redisTemplate.convertAndSend(topicName, location);
            log.info("Message published to topic [{}]: {}", topicName, location);
        } catch (Exception e) {
            log.error("Failed to publish message to topic [{}]: {}", topicName, location, e);
        }
    }

}

