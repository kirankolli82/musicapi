package com.kiran.stockapi.stockdata.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "quote-client")
public record QuoteClientProperties(String baseUrl, String gcpProjectId, String apiTokenSecretId) {
}
