package com.amalitech.gpuconfigurator.service.messageQueue.redis.publisher;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisherService {

    private final RedisPublisherImpl redisPublisher;

    private final ChannelTopic orderUpdateTopic;
    private final ChannelTopic stockUpdateTopic;

    public void publishToStockUpdates(Object message) {
        redisPublisher.publish(stockUpdateTopic.getTopic(), message);
    }

    public void publicToOrderUpdates(Object message) {
        redisPublisher.publish(orderUpdateTopic.getTopic(), message);
    }
}
