package com.amalitech.gpuconfigurator.config;


import com.amalitech.gpuconfigurator.service.messageQueue.redis.subscriber.RedisOrderTrackingListener;
import com.amalitech.gpuconfigurator.service.messageQueue.redis.subscriber.RedisStockUpdateListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${redis.pubsub.stockTopic:stock")
    private String stockUpdateTopic;

    @Value("${redis.pubsub.orderTopic:order")
    private String orderTrackingTopic;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public ChannelTopic stockUpdateTopic() {
        return new ChannelTopic(stockUpdateTopic);
    }
    @Bean ChannelTopic orderUpdateTopic() { return new ChannelTopic(orderTrackingTopic); };

    @Bean
    public MessageListenerAdapter stockUpdateMessageListenerAdapter(RedisStockUpdateListener redisStockUpdateListener) {
        return new MessageListenerAdapter(redisStockUpdateListener);
    }

    @Bean
    public MessageListenerAdapter orderTrackingMessageListenerAdapter(RedisOrderTrackingListener redisOrderTrackingListener) {
        return new MessageListenerAdapter(redisOrderTrackingListener);
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter stockUpdateMessageListenerAdapter,
                                            MessageListenerAdapter orderTrackingMessageListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(stockUpdateMessageListenerAdapter, stockUpdateTopic());
        container.addMessageListener(orderTrackingMessageListenerAdapter, orderUpdateTopic());

        return container;
    }
}
