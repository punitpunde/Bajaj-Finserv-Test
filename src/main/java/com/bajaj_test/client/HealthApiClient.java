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

    public void submitSolution(String accessToken, String sqlQuery) {
        String url = config.getApi().getBaseUrl() + config.getApi().getSubmitSolutionPath();
        logger.info("Submitting solution to URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // sets Authorization: Bearer <token>
        headers.setContentType(MediaType.APPLICATION_JSON);

        SolutionRequest solution = new SolutionRequest(sqlQuery);
        HttpEntity<SolutionRequest> request = new HttpEntity<>(solution, headers);

        try {
            String response = restTemplate.postForObject(url, request, String.class);
            logger.info("Submission response: {}", response);
        } catch (Exception e) {
            logger.error("Failed to submit solution: {}", e.getMessage(), e);
            throw e; // optionally rethrow or handle
        }
    }

}
