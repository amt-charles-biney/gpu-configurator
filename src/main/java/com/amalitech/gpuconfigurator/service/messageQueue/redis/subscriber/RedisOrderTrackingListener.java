package com.amalitech.gpuconfigurator.service.messageQueue.redis.subscriber;

import com.amalitech.gpuconfigurator.dto.email.EmailOrderRequest;
import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.service.email.OrderManagementEmailServiceImpl;
import com.amalitech.gpuconfigurator.service.order.OrderServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisOrderTrackingListener implements MessageListener {

    private final OrderServiceImpl orderService;
    private final OrderManagementEmailServiceImpl emailService;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String trackerId = message.toString().replace("\"", "");

        Order order = orderService.getOrderByTrackingCode(trackerId);

        EmailOrderRequest emailOrderRequest = EmailOrderRequest
                .builder()
                .email(order.getUser().getEmail())
                .orderStatus(order.getStatus())
                .orderId(order.getTrackingId())
                .message("the current order of " +  order.getTrackingId() +  "is " + order.getStatus())
                .build();

        try {
            emailService.send(emailOrderRequest);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
