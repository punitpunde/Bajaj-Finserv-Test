package com.bajaj_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WebhookService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SQLProblemService sqlProblemService;

    public void processWebhook() {
        try {
            String regNo = "358";
            Map<String, String> webhookData = generateWebhook(regNo);
            String webhookUrl = webhookData.get("webhookUrl");

            String sqlQuery;
            int lastTwoDigits = Integer.parseInt(regNo.substring(regNo.length() - 2));

            if (lastTwoDigits % 2 != 0) {
                log.info("Registration number {} is odd. Solving Question 1.", regNo);
                sqlQuery = sqlProblemService.solveQuestion1();
            } else {
                log.info("Registration number {} is even. Solving Question 2.", regNo);
                sqlQuery = sqlProblemService.solveQuestion2();
            }

            submitSolution(webhookUrl, sqlQuery);

        } catch (Exception e) {
            log.error("Error processing webhook", e);
        }
    }

    private Map<String, String> generateWebhook(String regNo) {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Punit Punde");
        requestBody.put("regNo", regNo);
        requestBody.put("email", "punitpunde@gmail.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        log.info("Generating webhook for registration number {}", regNo);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            String webhookUrl = (String) responseBody.get("webhook");
            String accessToken = (String) responseBody.get("accessToken");

            System.setProperty("accessToken", accessToken);
            log.info("Webhook generated successfully: {}", webhookUrl);

            Map<String, String> webhookData = new HashMap<>();
            webhookData.put("webhookUrl", webhookUrl);
            return webhookData;
        } else {
            throw new RuntimeException("Failed to generate webhook: " + response.getStatusCode());
        }
    }

    private void submitSolution(String webhookUrl, String sqlQuery) {
        String url = webhookUrl;

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("finalQuery", sqlQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", System.getProperty("accessToken"));

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        log.info("Submitting solution to: {}", url);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        log.info("Solution submitted. Response: {}", response.getBody());
    }
}
