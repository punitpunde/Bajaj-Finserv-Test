package com.bajaj_test.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bajaj")
public class AppConfigProperties {

    private final Api api = new Api();
    private final User user = new User();

    public Api getApi() { return api; }
    public User getUser() { return user; }

    public static class Api {
        private String baseUrl;
        private String generateWebhookPath;
        private String submitSolutionPath;
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String v) { this.baseUrl = v; }
        public String getGenerateWebhookPath() { return generateWebhookPath; }
        public void setGenerateWebhookPath(String v) { this.generateWebhookPath = v; }
        public String getSubmitSolutionPath() { return submitSolutionPath; }
        public void setSubmitSolutionPath(String v) { this.submitSolutionPath = v; }
    }

    public static class User {
        private String name;
        private String regNo;
        private String email;
        public String getName() { return name; }
        public void setName(String v) { this.name = v; }
        public String getRegNo() { return regNo; }
        public void setRegNo(String v) { this.regNo = v; }
        public String getEmail() { return email; }
        public void setEmail(String v) { this.email = v; }
    }
}
