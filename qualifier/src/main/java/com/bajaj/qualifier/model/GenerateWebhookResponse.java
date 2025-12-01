package com.bajaj.qualifier.model;

import lombok.Data;

@Data
public class GenerateWebhookResponse {
    private String webhookUrl;
    private String accessToken;
}
