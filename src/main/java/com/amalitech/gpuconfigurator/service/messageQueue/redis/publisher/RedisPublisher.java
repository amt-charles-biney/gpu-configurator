package com.amalitech.gpuconfigurator.service.messageQueue.redis.publisher;

public interface RedisPublisher {
    void publish(String channel, Object message);
}
