package com.amalitech.gpuconfigurator.dto.webhook.easypost;

import com.easypost.model.Tracker;
import lombok.Getter;


@Getter
public class TrackerResult extends Tracker {
    String tracking_code;
    String est_delivery_date;
}