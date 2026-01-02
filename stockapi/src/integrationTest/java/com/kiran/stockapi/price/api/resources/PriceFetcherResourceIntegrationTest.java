package com.kiran.stockapi.price.api.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kiran.stockapi.KafkaContainer;
import com.kiran.stockapi.PostgresContainer;
import com.kiran.stockapi.price.api.contract.PriceFetchTriggerResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class PriceFetcherResourceIntegrationTest {

	@Container
	static KafkaContainer KAFKA_CONTAINER = new KafkaContainer();

	@Container
	static PostgresContainer POSTGRESDB_CONTAINER = new PostgresContainer();

	@Autowired
	private TestRestTemplate restTemplate;

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
		registry.add("spring.datasource.url", POSTGRESDB_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", POSTGRESDB_CONTAINER::getUsername);
		registry.add("spring.datasource.password", POSTGRESDB_CONTAINER::getPassword);
	}

	@Test
	void testTriggerPriceFetchEndpoint() {
		// Act
		ResponseEntity<PriceFetchTriggerResponse> response = restTemplate.postForEntity("/api/price-fetcher/trigger",
				null, PriceFetchTriggerResponse.class);

		// Assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		PriceFetchTriggerResponse body = response.getBody();
		assertNotNull(body);
		assertEquals("SUCCESS", body.getStatus());
		assertEquals("Price fetch triggered successfully", body.getMessage());
		assertNotNull(body.getTriggeredAtInstant());

		log.info("Price fetch triggered successfully at: {}", body.getTriggeredAt());
	}
}
