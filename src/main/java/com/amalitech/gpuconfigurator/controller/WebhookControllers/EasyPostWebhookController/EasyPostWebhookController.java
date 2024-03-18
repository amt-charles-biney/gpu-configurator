package com.amalitech.gpuconfigurator.controller.WebhookControllers.EasyPostWebhookController;

import com.amalitech.gpuconfigurator.service.EasyPost.EasyPostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.easypost.model.Event;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EasyPostWebhookController {

    private final EasyPostService easyPostService;
    @CrossOrigin
    @PostMapping("/v1/webhook/easypost")
    public ResponseEntity<String> handleEasyPostWebhook(@RequestBody Event event) throws Exception {
        easyPostService.handleTrackerUpdatedEvent(event);
        return ResponseEntity.ok("Event received and processed successfully.");
    }
}
