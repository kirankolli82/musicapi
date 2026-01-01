package com.kiran.stockapi.stockdata.api.client;

import com.kiran.stockapi.stockdata.api.contract.StockApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

// @RestClient
public interface QuoteClient {
	@Path("data/quote")
	@GET
	StockApiResponse getQuote(@QueryParam("symbols") String symbol, @QueryParam("key_by_ticker") Boolean keyByTicker);
}
