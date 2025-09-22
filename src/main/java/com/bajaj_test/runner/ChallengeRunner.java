package com.bajaj_test.runner;

import com.bajaj_test.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Executes the challenge logic after the application starts.
 */
@Component
public class ChallengeRunner implements CommandLineRunner {

    private final WebhookService webhookService;

    public ChallengeRunner(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @Override
    public void run(String... args) throws Exception {
        webhookService.executeChallenge();
    }
}
