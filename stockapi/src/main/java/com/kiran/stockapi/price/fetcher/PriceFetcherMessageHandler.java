package com.kiran.stockapi.price.fetcher;

import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * Message handler for processing price fetch requests. Delegates to
 * PriceFetcherService to perform the actual price fetching.
 */
@Component
public class PriceFetcherMessageHandler implements MessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(PriceFetcherMessageHandler.class);

	private final PriceFetcherService priceFetcherService;

	public PriceFetcherMessageHandler(PriceFetcherService priceFetcherService) {
		this.priceFetcherService = priceFetcherService;
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		if (message.getPayload() instanceof ZonedDateTime timestamp) {
			logger.info("Processing price fetch request: {}", timestamp);
			priceFetcherService.fetchPrices(timestamp);
		} else {
			logger.warn("Unexpected message payload type: {}", message.getPayload().getClass());
		}
	}
}
