package io.abdulmajid.near_connect.websocket.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RedisPubSub {
    private final StringRedisTemplate redisTemplate;

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final RedisMessageSubscriber subscriber;

    private final Set<String> subscribedChannels = ConcurrentHashMap.newKeySet();

    public RedisPubSub(StringRedisTemplate redisTemplate, RedisMessageListenerContainer redisMessageListenerContainer,
                       MessageListenerAdapter listenerAdapter, RedisMessageSubscriber subscriber) {
        this.redisTemplate = redisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.subscriber = subscriber;
    }

    private void subscribeToChannel(Collection<String> userIds) {
        Set<ChannelTopic> topics = userIds.stream()
                .map(this::generateTopicName)
                .filter(subscribedChannels::add) // Only add new channels
                .map(ChannelTopic::new)
                .collect(Collectors.toSet());
        if (!topics.isEmpty()) {
            log.info("Subscribing to channels: {}", topics);
            redisMessageListenerContainer.addMessageListener(subscriber, topics);
        } else {
            log.info("No new channels to subscribe to.");
        }
    }


    // Set up dynamic Pub/Sub for the user
    private void setUpChannelTopic(String userId) {
        String topicName = generateTopicName(userId);
        ChannelTopic topic = new ChannelTopic(topicName);
        log.info("{} channel for redis created: {}", userId,topic.getTopic());
    }


    public void setupUserChannels(String userId, Set<String> otherUserIds) {
        log.info("User {} with friend {}", userId, otherUserIds.toString());
        setUpChannelTopic(userId);
        subscribeToChannel(otherUserIds);
    }

    private void unsubscribeFromChannel(Collection<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            log.warn("Cannot unsubscribe from null or empty channel names.");
        }

        Set<ChannelTopic> topicsToUnsubscribe = userIds.stream()
                .map(this::generateTopicName)
                .filter(subscribedChannels::remove) // Only process channels that are subscribed
                .map(ChannelTopic::new)
                .collect(Collectors.toSet());

        if (!topicsToUnsubscribe.isEmpty()) {
            log.info("Unsubscribing from channels: {}", topicsToUnsubscribe);
            redisMessageListenerContainer.removeMessageListener(subscriber, topicsToUnsubscribe);
        } else {
            log.info("No valid channels to unsubscribe from.");
        }
    }


    // Remove dynamic Pub/Sub for the user
    public void removeChannelTopic(String userId,Set<String> otherUserIds) {
        String topicName = generateTopicName(userId);
        unsubscribeFromChannel(otherUserIds);
        log.info("Unsubscribed from topic: {}", topicName);
    }

    private String generateTopicName(String userId) {
        return "user:" + userId + ":location";
    }

    public void publishLocation(String location, Collection<String> otherUserIds) {
        for (String friend : otherUserIds) {
            String topicName = generateTopicName(friend);
            try {
                // Publish the location message to the dynamically created channel
                redisTemplate.convertAndSend(topicName, location);
                log.info("Message published to channel topic [{}]: {}", topicName, location);
            } catch (Exception e) {
                log.error("Failed to publish message to topic [{}]: {}", topicName, location, e);
            }
        }
    }

}

