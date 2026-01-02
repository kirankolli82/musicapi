package com.kiran.stockapi.price.api.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Response DTO for price fetch trigger endpoint.
 */
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public final class PriceFetchTriggerResponse {

	private final String message;

	@EqualsAndHashCode.Exclude
	private final ZonedDateTime triggeredAt;

	private final String status;

	/**
	 * Returns the triggered time as Instant for equals/hashCode comparison. This
	 * ensures timezone-independent comparison.
	 */
	@JsonIgnore
	@EqualsAndHashCode.Include
	public Instant getTriggeredAtInstant() {
		return triggeredAt != null ? triggeredAt.toInstant() : null;
	}
}
