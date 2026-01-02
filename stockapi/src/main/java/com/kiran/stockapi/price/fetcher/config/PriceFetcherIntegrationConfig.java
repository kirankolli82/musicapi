package com.kiran.stockapi.price.fetcher.config;

import com.kiran.stockapi.price.fetcher.PriceFetcherMessageHandler;
import com.kiran.stockapi.price.fetcher.PriceFetcherMessageSource;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.CronTrigger;

/**
 * Spring Integration configuration for the price fetcher flow. This flow is
 * triggered every 3 hours between 9 AM EST and 5 PM EST. Trigger times: 9 AM,
 * 12 PM, 3 PM EST
 */
@Configuration
@EnableIntegration
@RequiredArgsConstructor
public class PriceFetcherIntegrationConfig {

	private static final Logger logger = LoggerFactory.getLogger(PriceFetcherIntegrationConfig.class);

	private final PriceFetcherMessageHandler priceFetcherMessageHandler;
	private final PriceFetcherMessageSource priceFetcherMessageSource;

	/**
	 * Input channel for the price fetcher flow.
	 */
	@Bean
	public MessageChannel priceFetcherInputChannel() {
		return new DirectChannel();
	}

	/**
	 * Output channel for the price fetcher flow.
	 */
	@Bean
	public MessageChannel priceFetcherOutputChannel() {
		return new DirectChannel();
	}

	/**
	 * Poller configuration for the price fetcher flow. Cron expression: 0 0 9,12,15
	 * * * ? - runs at 9 AM, 12 PM, and 3 PM EST every day This ensures the flow
	 * runs every 3 hours between 9 AM and 5 PM EST (last run at 3 PM).
	 */
	@Bean
	public PollerMetadata priceFetcherPoller() {
		PollerMetadata poller = new PollerMetadata();
		// Cron: sec min hour day month dayOfWeek
		// 0 0 9,12,15 * * ? = At 9 AM, 12 PM, and 3 PM every day
		CronTrigger cronTrigger = new CronTrigger("0 0 9,12,15 * * ?", TimeZone.getTimeZone("America/New_York"));
		poller.setTrigger(cronTrigger);
		return poller;
	}

	/**
	 * Integration flow that polls at scheduled times and processes price fetch
	 * requests.
	 */
	@Bean
	public IntegrationFlow priceFetcherFlow() {
		return IntegrationFlow.from(priceFetcherMessageSource, c -> c.poller(priceFetcherPoller()))
				.channel(priceFetcherInputChannel()).handle(priceFetcherMessageHandler)
				.channel(priceFetcherOutputChannel()).get();
	}

	/**
	 * Service activator that handles messages on the output channel. This is where
	 * post-processing or persistence logic would go.
	 */
	@ServiceActivator(inputChannel = "priceFetcherOutputChannel")
	public void handlePriceFetchComplete(Message<ZonedDateTime> message) {
		logger.info("Price fetch completed at: {}", message.getPayload());
		// TODO: Add post-processing logic here (e.g., save to database, send
		// notifications)
	}
}
