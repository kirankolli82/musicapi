package com.kiran.stockapi.price.fetcher;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit test for PriceFetcherService.
 */
@ExtendWith(MockitoExtension.class)
class PriceFetcherServiceTest {

	@InjectMocks
	private PriceFetcherService priceFetcherService;

	@Test
	void testFetchPrices() {
		ZonedDateTime triggerTime = ZonedDateTime.now();

		assertDoesNotThrow(() -> priceFetcherService.fetchPrices(triggerTime));
	}

	@Test
	void testFetchPricesWithNullTriggerTime() {
		assertDoesNotThrow(() -> priceFetcherService.fetchPrices(null));
	}
}
