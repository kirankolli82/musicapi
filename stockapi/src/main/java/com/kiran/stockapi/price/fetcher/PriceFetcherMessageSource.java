package com.kiran.stockapi.price.fetcher;

import java.time.ZonedDateTime;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * Message source that triggers price fetch requests. Generates messages
 * containing the current timestamp when polled.
 */
@Component
public class PriceFetcherMessageSource implements MessageSource<ZonedDateTime> {

	private static final Logger logger = LoggerFactory.getLogger(PriceFetcherMessageSource.class);

	@Value("${price-fetcher.enabled:true}")
	private boolean enabled;

	@Override
	public Message<ZonedDateTime> receive() {
		if (!enabled) {
			logger.debug("Price fetcher is disabled, skipping trigger");
			return null;
		}

		ZonedDateTime now = ZonedDateTime.now(TimeZone.getTimeZone("America/New_York").toZoneId());
		logger.info("Price fetcher triggered at: {}", now);
		return new GenericMessage<>(now);
	}
}
