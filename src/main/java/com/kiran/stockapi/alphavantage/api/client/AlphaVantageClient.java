package com.kiran.stockapi.alphavantage.api.client;

import com.kiran.stockapi.alphavantage.api.contract.RealtimeBulkQuotesResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

public interface AlphaVantageClient {
	@Path("query")
	@GET
	RealtimeBulkQuotesResponse getRealtimeBulkQuotes(
			@QueryParam("function") String function,
			@QueryParam("symbol") String symbols);
}
