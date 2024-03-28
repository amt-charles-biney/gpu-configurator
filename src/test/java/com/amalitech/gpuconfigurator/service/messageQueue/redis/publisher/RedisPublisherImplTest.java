package com.amalitech.gpuconfigurator.service.messageQueue.redis.publisher;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public class RedisPublisherImplTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ChannelTopic channelTopic;

    @InjectMocks
    private RedisPublisherImpl redisPublisher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPublish() {
        String attributeName = "testAttribute";
        redisPublisher.publish(attributeName);
        verify(redisTemplate, times(1)).convertAndSend(channelTopic.getTopic(), attributeName);
    }
}
