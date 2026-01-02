package com.kiran.stockapi.alphavantage.api.client;

import com.kiran.stockapi.alphavantage.api.config.AlphaVantageClientProperties;
import com.kiran.stockapi.common.gcp.SecretManagerService;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AlphaVantageClientRequestFilter implements ClientRequestFilter {
	private final AlphaVantageClientProperties alphaVantageClientProperties;
	private final SecretManagerService secretManagerService;

	@Override
	public void filter(ClientRequestContext requestContext) {
		// Fetch API key from Google Cloud Secret Manager
		String apiKey = secretManagerService.getSecret(alphaVantageClientProperties.gcpProjectId(),
				alphaVantageClientProperties.apiKeySecretId());

		URI uri = requestContext.getUri();
		URI newUri = UriBuilder.fromUri(uri).queryParam("apikey", apiKey).build();
		requestContext.setUri(newUri);
	}
}
