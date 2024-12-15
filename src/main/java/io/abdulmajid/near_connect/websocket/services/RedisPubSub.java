package io.abdulmajid.near_connect.websocket.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RedisPubSub {
    private final StringRedisTemplate redisTemplate;

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final MessageListenerAdapter listenerAdapter;

    private final RedisMessageSubscriber subscriber;


    public RedisPubSub(StringRedisTemplate redisTemplate, RedisMessageListenerContainer redisMessageListenerContainer,
                       MessageListenerAdapter listenerAdapter, RedisMessageSubscriber subscriber, RedisMessageListenerContainer redisContainer) {
        this.redisTemplate = redisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.listenerAdapter = listenerAdapter;
        this.subscriber = subscriber;
    }

    public void subscribeToChannel(String channelName) {
        log.info("Subscribing to channel: {}", channelName);
        redisMessageListenerContainer.addMessageListener(subscriber, new PatternTopic(channelName));
    }

    public void setupUserChannels(String userId, List<String> otherUserIds) {
        setUpChannelTopic(userId);
        for (String otherUserId : otherUserIds) {
            String otherUserChannel = generateTopicName(otherUserId);
            subscribeToChannel(otherUserChannel);
        }
    }

    public void unsubscribeFromChannel(String channelName) {
        log.info("Unsubscribing from channel: {}", channelName);
        redisMessageListenerContainer.removeMessageListener(subscriber, new PatternTopic(channelName));
    }


    // Set up dynamic Pub/Sub for the user
    public void setUpChannelTopic(String userId) {
        String topicName = generateTopicName(userId);
        ChannelTopic topic = new ChannelTopic(topicName);
        log.info("Subscribed to my own topic: {}", topic.getTopic());
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
            log.info("Message published to my channel topic [{}]: {}", topicName, location);
        } catch (Exception e) {
            log.error("Failed to publish message to topic [{}]: {}", topicName, location, e);
        }
    }

}

