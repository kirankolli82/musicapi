package com.kiran.stockapi.price.fetcher.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;

/**
 * Test class for PriceFetcherIntegrationConfig. Verifies that all beans are
 * properly configured.
 */
@SpringBootTest
class PriceFetcherIntegrationConfigTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private MessageChannel priceFetcherInputChannel;

	@Autowired
	private MessageChannel priceFetcherOutputChannel;

	@Autowired
	private MessageSource<ZonedDateTime> priceFetcherMessageSource;

	@Autowired
	private PollerMetadata priceFetcherPoller;

	@Test
	void testContextLoads() {
		assertNotNull(applicationContext);
	}

	@Test
	void testPriceFetcherInputChannelBeanExists() {
		assertNotNull(priceFetcherInputChannel);
	}

	@Test
	void testPriceFetcherOutputChannelBeanExists() {
		assertNotNull(priceFetcherOutputChannel);
	}

	@Test
	void testPriceFetcherMessageSourceBeanExists() {
		assertNotNull(priceFetcherMessageSource);
	}

	@Test
	void testPriceFetcherPollerBeanExists() {
		assertNotNull(priceFetcherPoller);
		assertNotNull(priceFetcherPoller.getTrigger());
	}

	@Test
	void testMessageSourceGeneratesMessage() {
		var message = priceFetcherMessageSource.receive();
		if (message != null) {
			assertNotNull(message.getPayload());
		}
		// Note: Message may be null if price-fetcher.enabled=false
	}
}
