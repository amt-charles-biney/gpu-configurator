package com.amalitech.gpuconfigurator.service.EasyPost;

import com.amalitech.gpuconfigurator.controller.WebhookControllers.EasyPostWebhookController.EasyPostWebhookController;
import com.amalitech.gpuconfigurator.dto.email.EmailOrderRequest;
import com.amalitech.gpuconfigurator.dto.webhook.easypost.TrackerResult;
import com.amalitech.gpuconfigurator.service.messageQueue.redis.publisher.RedisPublisherService;
import com.amalitech.gpuconfigurator.service.order.OrderServiceImpl;
import com.amalitech.gpuconfigurator.service.status.StatusService;
import com.easypost.model.Event;
import com.easypost.model.Tracker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class EasyPostService {

    private final OrderServiceImpl orderService;
    private final ObjectMapper objectMapper;
    private final RedisPublisherService redisPublisherService;

    public void handleTrackerUpdatedEvent(Event event) throws Exception {
        if("tracker.updated".equals(event.getDescription()) || "tracker.created".equals(event.getDescription())) {

            TrackerResult tracker = objectMapper.convertValue(event.getResult(), TrackerResult.class);
            String trackerId = tracker.getTracking_code();
            String status = tracker.getStatus();
            String deliveryDate = tracker.getEst_delivery_date();

            String trackerStatus = StatusService.mapEasyPostStatus(status);
            orderService.updateStatusByTrackingCode(trackerId, trackerStatus, deliveryDate);
            redisPublisherService.publicToOrderUpdates(trackerId);
        }
    }
}
