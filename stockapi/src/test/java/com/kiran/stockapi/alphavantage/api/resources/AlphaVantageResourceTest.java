package com.kiran.stockapi.alphavantage.api.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kiran.stockapi.alphavantage.api.client.AlphaVantageClient;
import com.kiran.stockapi.alphavantage.api.contract.RealtimeBulkQuotesResponse;
import com.kiran.stockapi.alphavantage.api.contract.StockQuote;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlphaVantageResourceTest {

	private AlphaVantageResource resource;
	private AlphaVantageClient alphaVantageClient;

	@BeforeEach
	void setUp() {
		alphaVantageClient = mock(AlphaVantageClient.class);
		resource = new AlphaVantageResource(alphaVantageClient);
	}

	@Test
	void testGetRealtimeBulkQuotes() {
		// Arrange
		String symbols = "AAPL,MSFT";
		StockQuote quote1 = StockQuote.builder().symbol("AAPL").timestamp("2026-01-01 16:00:00")
				.open(new BigDecimal("150.00")).high(new BigDecimal("155.00")).low(new BigDecimal("149.00"))
				.close(new BigDecimal("154.50")).volume("1000000").build();

		StockQuote quote2 = StockQuote.builder().symbol("MSFT").timestamp("2026-01-01 16:00:00")
				.open(new BigDecimal("300.00")).high(new BigDecimal("305.00")).low(new BigDecimal("299.00"))
				.close(new BigDecimal("304.50")).volume("500000").build();

		RealtimeBulkQuotesResponse expectedResponse = RealtimeBulkQuotesResponse.builder()
				.endpoint("REALTIME_BULK_QUOTES").message("Success").data(List.of(quote1, quote2)).build();

		when(alphaVantageClient.getRealtimeBulkQuotes(eq("REALTIME_BULK_QUOTES"), eq(symbols)))
				.thenReturn(expectedResponse);

		// Act
		RealtimeBulkQuotesResponse actualResponse = resource.getRealtimeBulkQuotes(symbols);

		// Assert
		assertNotNull(actualResponse);
		assertEquals("REALTIME_BULK_QUOTES", actualResponse.getEndpoint());
		assertEquals("Success", actualResponse.getMessage());
		assertEquals(2, actualResponse.getData().size());
		verify(alphaVantageClient).getRealtimeBulkQuotes("REALTIME_BULK_QUOTES", symbols);
	}

	@Test
	void testGetRealtimeBulkQuotesWithDefaultSymbols() {
		// Arrange
		String defaultSymbols = "GRID,MSFT,AAPL,IBM";
		RealtimeBulkQuotesResponse expectedResponse = RealtimeBulkQuotesResponse.builder()
				.endpoint("REALTIME_BULK_QUOTES").message("Success").data(List.of()).build();

		when(alphaVantageClient.getRealtimeBulkQuotes(eq("REALTIME_BULK_QUOTES"), eq(defaultSymbols)))
				.thenReturn(expectedResponse);

		// Act
		RealtimeBulkQuotesResponse actualResponse = resource.getRealtimeBulkQuotes(defaultSymbols);

		// Assert
		assertNotNull(actualResponse);
		verify(alphaVantageClient).getRealtimeBulkQuotes("REALTIME_BULK_QUOTES", defaultSymbols);
	}
}
