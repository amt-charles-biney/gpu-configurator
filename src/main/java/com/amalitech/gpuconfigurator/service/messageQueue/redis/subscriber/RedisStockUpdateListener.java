package com.amalitech.gpuconfigurator.service.messageQueue.redis.subscriber;

import com.amalitech.gpuconfigurator.dto.email.EmailStockRequest;
import com.amalitech.gpuconfigurator.dto.email.EmailTemplateRequest;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.service.email.NotifyStockUpdateEmailServiceImpl;
import com.amalitech.gpuconfigurator.service.notification.NotificationServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisStockUpdateListener implements MessageListener {

    private final NotificationServiceImpl notificationService;
    private final NotifyStockUpdateEmailServiceImpl emailService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String attributeName = message.toString().replace("\"", "");

        Map<User, Product> usersToProduct = notificationService.getUsersToNotifyOfStockUpdate(attributeName);

        for (Map.Entry<User, Product> entry : usersToProduct.entrySet()) {
            User user = entry.getKey();
            Product product = entry.getValue();
            if(user != null) {
                EmailStockRequest emailStockRequest = EmailStockRequest
                        .builder()
                        .email(user != null ? user.getEmail() : "blank@email.com")
                        .message("Product " + product.getProductName() + " has been updated in stock value")
                        .productId(product.getId().toString())
                        .build();

                try {
                    emailService.send(emailStockRequest);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
