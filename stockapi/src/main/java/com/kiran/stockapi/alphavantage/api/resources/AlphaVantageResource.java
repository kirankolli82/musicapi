package com.kiran.stockapi.alphavantage.api.resources;

import com.kiran.stockapi.alphavantage.api.client.AlphaVantageClient;
import com.kiran.stockapi.alphavantage.api.contract.RealtimeBulkQuotesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AlphaVantageResource {

	private final AlphaVantageClient alphaVantageClient;

	public AlphaVantageResource(AlphaVantageClient alphaVantageClient) {
		this.alphaVantageClient = alphaVantageClient;
	}

	@GetMapping("/alphavantage/realtime-bulk-quotes")
	public RealtimeBulkQuotesResponse getRealtimeBulkQuotes(
			@RequestParam(defaultValue = "GRID,MSFT,AAPL,IBM") String symbols) {
		log.info("Fetching realtime bulk quotes for symbols: {}", symbols);
		return alphaVantageClient.getRealtimeBulkQuotes("REALTIME_BULK_QUOTES", symbols);
	}
}

