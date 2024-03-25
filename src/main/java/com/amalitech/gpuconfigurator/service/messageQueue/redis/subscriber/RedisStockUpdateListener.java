package com.amalitech.gpuconfigurator.service.messageQueue.redis.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStockUpdateListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // get the users from wishlist
        // get the product and the category stock and the update value
        // call the email sender to send emails to each user emails
    }


}
