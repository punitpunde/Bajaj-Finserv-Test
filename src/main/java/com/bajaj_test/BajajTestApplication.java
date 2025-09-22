package com.bajaj_test;

import com.bajaj_test.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BajajTestApplication {
    @Autowired
    private WebhookService webhookService;

    public static void main(String[] args) {
        SpringApplication.run(BajajTestApplication.class, args);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        webhookService.processWebhook();
    }
}