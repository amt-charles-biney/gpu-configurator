package com.amalitech.gpuconfigurator.service.status;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatusService {
    private static final Map<String, String> mapping = new HashMap<>();

    static {
        mapping.put("pre_transit", "Pending");
        mapping.put("in_transit", "Shipped");
        mapping.put("out_for_delivery", "Out For Delivery");
        mapping.put("delivered", "Delivered");
        mapping.put("available_for_pickup", "Delivered");
        mapping.put("return_to_sender", "Cancelled");
        mapping.put("failure", "Cancelled");
        mapping.put("cancelled", "Cancelled");
        mapping.put("unknown", "Pending");
    }

    public static String mapEasyPostStatus(String easyPostStatus) {
        return mapping.getOrDefault(easyPostStatus, "Pending");
    }

}
