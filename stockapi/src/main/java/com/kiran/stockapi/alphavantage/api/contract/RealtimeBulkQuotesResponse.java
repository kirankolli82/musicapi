package com.kiran.stockapi.alphavantage.api.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public final class RealtimeBulkQuotesResponse {

	@JsonProperty("endpoint")
	private final String endpoint;

	@JsonProperty("message")
	private final String message;

	@JsonProperty("data")
	private final List<StockQuote> data;
}
