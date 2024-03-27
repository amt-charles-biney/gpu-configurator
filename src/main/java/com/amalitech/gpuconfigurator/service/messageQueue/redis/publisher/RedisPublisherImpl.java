package com.amalitech.gpuconfigurator.service.messageQueue.redis.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisherImpl implements RedisPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ChannelTopic channelTopic;

    @Override
    public void publish(String attributeName) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), attributeName);
    }

}
