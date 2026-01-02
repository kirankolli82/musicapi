package com.kiran.stockapi.alphavantage.api.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
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
public final class StockQuote {

	@JsonProperty("symbol")
	private final String symbol;

	@JsonProperty("timestamp")
	private final String timestamp;

	@JsonProperty("open")
	@EqualsAndHashCode.Exclude
	private final BigDecimal open;

	@JsonProperty("high")
	@EqualsAndHashCode.Exclude
	private final BigDecimal high;

	@JsonProperty("low")
	@EqualsAndHashCode.Exclude
	private final BigDecimal low;

	@JsonProperty("close")
	@EqualsAndHashCode.Exclude
	private final BigDecimal close;

	@JsonProperty("volume")
	private final String volume;

	@JsonProperty("previous_close")
	@EqualsAndHashCode.Exclude
	private final BigDecimal previousClose;

	@JsonProperty("change")
	@EqualsAndHashCode.Exclude
	private final BigDecimal change;

	@JsonProperty("change_percent")
	private final String changePercent;

	@JsonProperty("extended_hours_quote")
	private final String extendedHoursQuote;

	@JsonProperty("extended_hours_change")
	private final String extendedHoursChange;

	@JsonProperty("extended_hours_change_percent")
	private final String extendedHoursChangePercent;

	@JsonIgnore
	@EqualsAndHashCode.Include
	public BigDecimal getOpenStripped() {
		return open != null ? open.stripTrailingZeros() : null;
	}

	@JsonIgnore
	@EqualsAndHashCode.Include
	public BigDecimal getHighStripped() {
		return high != null ? high.stripTrailingZeros() : null;
	}

	@JsonIgnore
	@EqualsAndHashCode.Include
	public BigDecimal getLowStripped() {
		return low != null ? low.stripTrailingZeros() : null;
	}

	@JsonIgnore
	@EqualsAndHashCode.Include
	public BigDecimal getCloseStripped() {
		return close != null ? close.stripTrailingZeros() : null;
	}

	@JsonIgnore
	@EqualsAndHashCode.Include
	public BigDecimal getPreviousCloseStripped() {
		return previousClose != null ? previousClose.stripTrailingZeros() : null;
	}

	@JsonIgnore
	@EqualsAndHashCode.Include
	public BigDecimal getChangeStripped() {
		return change != null ? change.stripTrailingZeros() : null;
	}
}
