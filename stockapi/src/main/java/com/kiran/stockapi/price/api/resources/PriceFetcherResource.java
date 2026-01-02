package com.kiran.stockapi.price.api.resources;

import com.kiran.stockapi.price.api.contract.PriceFetchTriggerResponse;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST resource for triggering price fetcher operations on-demand. Sends
 * messages to the Spring Integration flow via the input channel.
 */
@Slf4j
@RestController
@RequestMapping("/api/price-fetcher")
public class PriceFetcherResource {

	private final MessageChannel priceFetcherInputChannel;

	public PriceFetcherResource(MessageChannel priceFetcherInputChannel) {
		this.priceFetcherInputChannel = priceFetcherInputChannel;
	}

	/**
	 * Triggers an on-demand price fetch operation by sending a message to the
	 * Spring Integration flow. This uses the same flow as the scheduled trigger.
	 *
	 * @return response containing trigger status and timestamp
	 */
	@PostMapping("/trigger")
	public PriceFetchTriggerResponse triggerPriceFetch() {
		ZonedDateTime triggerTime = ZonedDateTime.now(TimeZone.getTimeZone("America/New_York").toZoneId());
		log.info("On-demand price fetch triggered at: {}", triggerTime);

		try {
			boolean sent = priceFetcherInputChannel.send(new GenericMessage<>(triggerTime));
			if (sent) {
				return PriceFetchTriggerResponse.builder().message("Price fetch triggered successfully")
						.triggeredAt(triggerTime).status("SUCCESS").build();
			} else {
				log.warn("Failed to send message to price fetcher input channel");
				return PriceFetchTriggerResponse.builder()
						.message("Price fetch failed: Unable to send message to channel").triggeredAt(triggerTime)
						.status("ERROR").build();
			}
		} catch (Exception e) {
			log.error("Error triggering price fetch", e);
			return PriceFetchTriggerResponse.builder().message("Price fetch failed: " + e.getMessage())
					.triggeredAt(triggerTime).status("ERROR").build();
		}
	}
}
