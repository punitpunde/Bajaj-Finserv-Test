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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        return restTemplate.postForObject(url, payload, WebhookResponse.class);
    }

    public void submitSolution(String accessToken, String finalQuery) {
        String url = config.getApi().getBaseUrl() + config.getApi().getSubmitSolutionPath();
        logger.info("Submitting solution to URL: {}", url);

        try {
            HttpHeaders headers = new HttpHeaders();
            // FIX: Added "Bearer " prefix for proper JWT authentication.
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Content-Type", "application/json");

            SolutionRequest solutionRequest = new SolutionRequest(finalQuery);
            HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);

            restTemplate.postForObject(url, entity, Map.class);
            logger.info("Solution submitted successfully.");

        } catch (Exception e) {
            logger.error("Failed to submit solution: {}", e.getMessage(), e);
            throw e; // Re-throw to be caught by the main service
        }
    }

}
