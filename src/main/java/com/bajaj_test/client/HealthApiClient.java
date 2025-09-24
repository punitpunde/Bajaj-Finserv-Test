package com.bajaj_test.client;

import com.bajaj_test.config.AppConfigProperties;
import com.bajaj_test.dto.SolutionRequest;
import com.bajaj_test.dto.WebhookRequest;
import com.bajaj_test.dto.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class HealthApiClient {
    private static final Logger logger = LoggerFactory.getLogger(HealthApiClient.class);
    private final RestTemplate restTemplate;
    private final AppConfigProperties config;

    public HealthApiClient(RestTemplate restTemplate, AppConfigProperties config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public WebhookResponse generateWebhook(WebhookRequest payload) {
        String url = config.getApi().getBaseUrl() + config.getApi().getGenerateWebhookPath();
        logger.info("Generating webhook at URL: {}", url);
        logger.debug("Webhook request payload: {}", payload);

        WebhookResponse response = restTemplate.postForObject(url, payload, WebhookResponse.class);

        if (response != null) {
            logger.info("Webhook generated successfully. Response: {}", response);
        } else {
            logger.warn("Webhook generation returned null response.");
        }
        return response;
    }

    public void submitSolution(String webhookUrl, String accessToken, String finalQuery) {
        String url = webhookUrl;
        logger.info("Submitting solution to URL: {}", url);
        logger.debug("Final query: {}", finalQuery);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("finalQuery", finalQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            logger.info("Solution submitted. Response status: {}", response.getStatusCode());
            logger.debug("Response body: {}", response.getBody());
        } catch (Exception e) {
            logger.error("Error submitting solution to URL: {}", url, e);
            throw e;
        }
    }
}
