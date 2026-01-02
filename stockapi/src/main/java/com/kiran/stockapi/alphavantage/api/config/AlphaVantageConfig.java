package com.kiran.stockapi.alphavantage.api.config;

import com.kiran.stockapi.alphavantage.api.client.AlphaVantageClient;
import com.kiran.stockapi.alphavantage.api.client.AlphaVantageClientRequestFilter;
import jakarta.ws.rs.client.ClientBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(AlphaVantageClientProperties.class)
@AllArgsConstructor
public class AlphaVantageConfig {

	private final AlphaVantageClientProperties alphaVantageClientProperties;
	private final AlphaVantageClientRequestFilter alphaVantageClientRequestFilter;

	@Bean
	public AlphaVantageClient alphaVantageClient() {
		var client = ClientBuilder.newBuilder().register(alphaVantageClientRequestFilter).build();
		var target = client.target(alphaVantageClientProperties.baseUrl());
		return ((ResteasyWebTarget) target).proxy(AlphaVantageClient.class);
	}
}
