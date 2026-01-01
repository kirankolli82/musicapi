package com.kiran.stockapi.alphavantage.api.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kiran.stockapi.alphavantage.api.config.AlphaVantageClientProperties;
import com.kiran.stockapi.common.gcp.SecretManagerService;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlphaVantageClientRequestFilterTest {

	private AlphaVantageClientRequestFilter filter;
	private SecretManagerService secretManagerService;
	private AlphaVantageClientProperties properties;
	private ClientRequestContext requestContext;

	@BeforeEach
	void setUp() {
		secretManagerService = mock(SecretManagerService.class);
		properties = new AlphaVantageClientProperties(
				"https://www.alphavantage.co",
				"test-project",
				"alpha_vantage_access_key");
		filter = new AlphaVantageClientRequestFilter(properties, secretManagerService);
		requestContext = mock(ClientRequestContext.class);
	}

	@Test
	void testFilterAddsApiKeyToRequest() {
		// Arrange
		String testApiKey = "test-api-key-123";
		URI originalUri = URI.create("https://www.alphavantage.co/query?function=REALTIME_BULK_QUOTES&symbol=AAPL");

		when(secretManagerService.getSecret(anyString(), anyString())).thenReturn(testApiKey);
		when(requestContext.getUri()).thenReturn(originalUri);

		// Act
		filter.filter(requestContext);

		// Assert
		verify(secretManagerService).getSecret("test-project", "alpha_vantage_access_key");
		verify(requestContext).setUri(any(URI.class));
	}

	@Test
	void testFilterBuildsCorrectUriWithApiKey() {
		// Arrange
		String testApiKey = "test-api-key-123";
		URI originalUri = URI.create("https://www.alphavantage.co/query?function=REALTIME_BULK_QUOTES&symbol=AAPL");

		when(secretManagerService.getSecret(anyString(), anyString())).thenReturn(testApiKey);

		// Act
		URI expectedUri = UriBuilder.fromUri(originalUri).queryParam("apikey", testApiKey).build();

		// Assert
		assertNotNull(expectedUri);
		assertEquals("https://www.alphavantage.co/query?function=REALTIME_BULK_QUOTES&symbol=AAPL&apikey=test-api-key-123",
				expectedUri.toString());
	}
}

