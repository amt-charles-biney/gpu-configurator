package com.amalitech.gpuconfigurator.service.messageQueue.redis.subscriber;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.service.email.EmailServiceImpl;
import com.amalitech.gpuconfigurator.service.notification.NotificationServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;

import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

class RedisStockUpdateListenerTest {
    @Mock
    private NotificationServiceImpl notificationService;

    @Mock
    private EmailServiceImpl emailService;

    @InjectMocks
    private RedisStockUpdateListener redisStockUpdateListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnMessage() throws MessagingException {

        String attributeName = "testAttribute";
        Map<User, Product> usersToProduct = new HashMap<>();

        User user = new User();
        Product product = new Product();

        product.setProductName("Test Product");
        user.setEmail("test@example.com");

        usersToProduct.put(user, product);

        when(notificationService.getUsersToNotifyOfStockUpdate(attributeName)).thenReturn(usersToProduct);


        Message message = mock(Message.class);

        when(message.toString()).thenReturn(attributeName);
        redisStockUpdateListener.onMessage(message, new byte[0]);

        verify(emailService, times(1)).sendGenericEmail(any());
    }
}