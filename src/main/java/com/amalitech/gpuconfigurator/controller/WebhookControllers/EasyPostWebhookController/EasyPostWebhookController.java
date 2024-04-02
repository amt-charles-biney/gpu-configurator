package com.amalitech.gpuconfigurator.controller.WebhookControllers.EasyPostWebhookController;

import com.amalitech.gpuconfigurator.service.EasyPost.EasyPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.easypost.model.Event;

@RestController
@RequestMapping("/api")
@Tag(name = "webhook")
@RequiredArgsConstructor
public class EasyPostWebhookController {

    private final EasyPostService easyPostService;

    @Operation(
            description = "Webhook Events",
            summary = "This is for listening to webhooks from shipping carrier",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/webhook/easypost")
    public ResponseEntity<String> handleEasyPostWebhook(@RequestBody Event event) throws Exception {
        easyPostService.handleTrackerUpdatedEvent(event);
        return ResponseEntity.ok("Event received and processed successfully.");
    }
}
