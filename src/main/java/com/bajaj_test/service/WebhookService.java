package com.bajaj_test.service;

import com.bajaj_test.client.HealthApiClient;
import com.bajaj_test.config.AppConfigProperties;
import com.bajaj_test.dto.WebhookRequest;
import com.bajaj_test.dto.WebhookResponse;
import com.bajaj_test.repository.EmployeeRepository;
import com.bajaj_test.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);

    private final HealthApiClient apiClient;
    private final AppConfigProperties config;
    private final SQLProblemService sqlProblemService;

    public WebhookService(HealthApiClient apiClient, AppConfigProperties config, SQLProblemService sqlProblemService) {
        this.apiClient = apiClient;
        this.config = config;
        this.sqlProblemService = sqlProblemService;
    }

    public void executeChallenge() {
        try {
            logger.info("Starting challenge for user: {}", config.getUser().getName());
            WebhookRequest request = new WebhookRequest(config.getUser().getName(), config.getUser().getRegNo(), config.getUser().getEmail());

            WebhookResponse response = apiClient.generateWebhook(request);

            String regNo = config.getUser().getRegNo();
            String sqlQuery = getQueryForRegistration(regNo);

            apiClient.submitSolution(response.getAccessToken(), sqlQuery);
            logger.info("Challenge completed successfully!");

        } catch (Exception e) {
            logger.error("Challenge execution failed: {}", e.getMessage(), e);
        }
    }

    private String getQueryForRegistration(String regNo) {
        if (isRegNoEven(regNo)) {
            logger.info("Registration number's last two digits are EVEN. Fetching Query 2.");
            return sqlProblemService.solveQuestion2();
        } else {
            logger.info("Registration number's last two digits are ODD. Fetching Query 1.");
            return sqlProblemService.solveQuestion1();
        }
    }

    /**
     * Checks if the last two digits of the registration number form an even number.
     * This logic adheres to the specific requirement from the challenge description.
     *
     * @param regNo The registration number string.
     * @return true if the last two digits form an even number, false otherwise.
     */
    private boolean isRegNoEven(String regNo) {
        if (regNo == null || regNo.length() < 2) {
            return false;
        }

        // Extract the substring of the last two characters
        String lastTwoChars = regNo.substring(regNo.length() - 2);

        try {
            // Parse the substring into an integer
            int lastTwoDigits = Integer.parseInt(lastTwoChars);
            return lastTwoDigits % 2 == 0;
        } catch (NumberFormatException e) {
            // If the last two characters are not digits, it cannot be even/odd.
            logger.warn("Could not parse the last two digits of the registration number: {}", regNo);
            return false;
        }
    }
}

