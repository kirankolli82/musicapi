package com.kiran.stockapi.alphavantage.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alphavantage-client")
public record AlphaVantageClientProperties(String baseUrl, String gcpProjectId, String apiKeySecretId) {
}
