package com.bajaj.qualifier.service;

import com.bajaj.qualifier.model.GenerateWebhookRequest;
import com.bajaj.qualifier.model.GenerateWebhookResponse;
import com.bajaj.qualifier.model.FinalQueryRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private static final String GENERATE_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    // YOUR DETAILS
    private static final String NAME = "Vinodhini S";
    private static final String REG_NO = "22BKT0156";
    private static final String EMAIL = "vinodhini4667@gmail.com";

    @PostConstruct
    public void onStart() {

        try {
            RestTemplate client = new RestTemplate();

            // Step 1 — Generate webhook
            GenerateWebhookRequest req =
                    new GenerateWebhookRequest(NAME, REG_NO, EMAIL);

            HttpHeaders h = new HttpHeaders();
            h.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<GenerateWebhookRequest> entity = new HttpEntity<>(req, h);

            ResponseEntity<GenerateWebhookResponse> resp =
                    client.exchange(GENERATE_URL, HttpMethod.POST, entity,
                            GenerateWebhookResponse.class);

            GenerateWebhookResponse body = resp.getBody();

            System.out.println("Webhook URL: " + body.getWebhookUrl());
            System.out.println("Access Token: " + body.getAccessToken());

            // Step 2 — Solve SQL question (for 252117 → QUESTION 1)
           String finalSQL = """
        SELECT b.Name AS BrandName, COUNT(p.product_id) AS number_of_products
        FROM product p
        JOIN brand b ON b.brand_id = p.brand_id
        GROUP BY b.Name
        ORDER BY number_of_products DESC;
        """;

            // Step 3 — Send SQL answer to webhook
            FinalQueryRequest finalReq = new FinalQueryRequest(finalSQL);

            HttpHeaders h2 = new HttpHeaders();
            h2.setContentType(MediaType.APPLICATION_JSON);
            h2.set("Authorization", body.getAccessToken());

            HttpEntity<FinalQueryRequest> entity2 =
                    new HttpEntity<>(finalReq, h2);

            ResponseEntity<String> submitResp =
                    client.exchange(body.getWebhookUrl(), HttpMethod.POST, entity2, String.class);

            System.out.println("Server Response: " + submitResp.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
