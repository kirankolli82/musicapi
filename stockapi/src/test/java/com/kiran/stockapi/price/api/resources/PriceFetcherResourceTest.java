package com.kiran.stockapi.price.api.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kiran.stockapi.price.api.contract.PriceFetchTriggerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

class PriceFetcherResourceTest {

	private PriceFetcherResource resource;
	private MessageChannel priceFetcherInputChannel;

	@BeforeEach
	void setUp() {
		priceFetcherInputChannel = mock(MessageChannel.class);
		resource = new PriceFetcherResource(priceFetcherInputChannel);
	}

	@Test
	void testTriggerPriceFetchSuccess() {
		// Arrange
		when(priceFetcherInputChannel.send(any(Message.class))).thenReturn(true);

		// Act
		PriceFetchTriggerResponse response = resource.triggerPriceFetch();

		// Assert
		assertNotNull(response);
		assertEquals("SUCCESS", response.getStatus());
		assertEquals("Price fetch triggered successfully", response.getMessage());
		assertNotNull(response.getTriggeredAt());
		verify(priceFetcherInputChannel).send(any(Message.class));
	}

	@Test
	void testTriggerPriceFetchFailureChannelReturnsFalse() {
		// Arrange
		when(priceFetcherInputChannel.send(any(Message.class))).thenReturn(false);

		// Act
		PriceFetchTriggerResponse response = resource.triggerPriceFetch();

		// Assert
		assertNotNull(response);
		assertEquals("ERROR", response.getStatus());
		assertEquals("Price fetch failed: Unable to send getMessage to channel", response.getMessage());
		assertNotNull(response.getTriggeredAt());
		verify(priceFetcherInputChannel).send(any(Message.class));
	}

	@Test
	void testTriggerPriceFetchFailureChannelThrowsException() {
		// Arrange
		when(priceFetcherInputChannel.send(any(Message.class)))
				.thenThrow(new RuntimeException("Test exception"));

		// Act
		PriceFetchTriggerResponse response = resource.triggerPriceFetch();

		// Assert
		assertNotNull(response);
		assertEquals("ERROR", response.getStatus());
		assertEquals("Price fetch failed: Test exception", response.getMessage());
		assertNotNull(response.getTriggeredAt());
		verify(priceFetcherInputChannel).send(any(Message.class));
	}
}
