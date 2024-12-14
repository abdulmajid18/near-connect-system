package io.abdulmajid.near_connect.websocket.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisPubSub {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisMessageSubscriber redisMessageSubscriber;

    private final MessageListenerAdapter listenerAdapter;

    public RedisPubSub(RedisMessageListenerContainer redisMessageListenerContainer,
                       RedisMessageSubscriber redisMessageSubscriber, MessageListenerAdapter listenerAdapter) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.redisMessageSubscriber = redisMessageSubscriber;
        this.listenerAdapter = listenerAdapter;
    }

    // Set up dynamic Pub/Sub for the user
    public void setUpPubSub(String userId) {
        String topicName = "user:" + userId + ":location";
        ChannelTopic topic = new ChannelTopic(topicName);

        redisMessageListenerContainer.addMessageListener(listenerAdapter, topic);
        log.info("Subscribed to topic: {}", topicName);
    }

    // Remove dynamic Pub/Sub for the user
    public void removePubSub(String userId) {
        String topicName = "user:" + userId + ":location";
        ChannelTopic topic = new ChannelTopic(topicName);

        // Remove the message listener from the container for the user topic
        redisMessageListenerContainer.removeMessageListener(listenerAdapter, topic);
        log.info("Unsubscribed from topic: {}", topicName);
    }
}

