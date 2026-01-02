# Price Fetcher - Setup and Configuration Guide

## Summary
A Spring Integration Flow has been created in the `com.kiran.stockapi.price.fetcher` package that automatically triggers price fetching at predetermined times during market hours.

## What Was Created

### 1. Package Structure
```
com.kiran.stockapi.price.fetcher/
├── config/
│   └── PriceFetcherIntegrationConfig.java
└── PriceFetcherService.java
```

### 2. Main Components

#### PriceFetcherIntegrationConfig (config package)
- **Purpose**: Configures the Spring Integration flow
- **Schedule**: Every 3 hours between 9 AM - 5 PM EST (9 AM, 12 PM, 3 PM)
- **Timezone**: America/New_York (EST/EDT)
- **Configuration**: All configuration is in Java (no XML)

**Key Features:**
- Cron-based polling using `0 0 9,12,15 * * ?` cron expression
- Configurable enable/disable via `price-fetcher.enabled` property
- Automatic timezone handling for EST/EDT transitions
- Input and output channels for message processing
- Integration with PriceFetcherService for business logic

#### PriceFetcherService
- **Purpose**: Contains the business logic for fetching prices
- **Current State**: Placeholder implementation with logging
- **Future**: Will be expanded to call external APIs and persist data

### 3. Dependencies Added to build.gradle
```groovy
implementation 'org.springframework.boot:spring-boot-starter-integration'
implementation 'org.springframework.integration:spring-integration-core'
```

### 4. Configuration Properties (application.properties)
```properties
# Spring Integration
spring.integration.poller.fixed-delay=1000
spring.integration.poller.max-messages-per-poll=1

# Price Fetcher Integration Flow
# Cron schedule: 0 0 9,12,15 * * ? (9 AM, 12 PM, 3 PM EST)
# Timezone: America/New_York (EST/EDT)
price-fetcher.enabled=true
```

### 5. Tests Created
- `PriceFetcherIntegrationConfigTest`: Tests configuration beans and flow setup
- `PriceFetcherServiceTest`: Tests the price fetcher service

## How It Works

### Flow Execution
1. **Cron Trigger** fires at 9 AM, 12 PM, and 3 PM EST
2. **Message Source** generates a message with current `ZonedDateTime`
3. **Input Channel** receives the message
4. **Handler** processes the message:
   - Extracts the trigger time
   - Calls `PriceFetcherService.fetchPrices()`
   - Logs the processing
5. **Output Channel** receives the processed message
6. **Service Activator** performs post-processing (logging, future: persistence)

### Schedule Details
- **Cron Expression**: `0 0 9,12,15 * * ?`
  - `0` seconds
  - `0` minutes (top of the hour)
  - `9,12,15` hours (9 AM, 12 PM, 3 PM)
  - `*` every day of month
  - `*` every month
  - `?` day of week ignored

- **Timezone**: America/New_York (automatically handles EST/EDT)
  - Ensures consistent execution during market hours
  - Automatically adjusts for daylight saving time

## Next Steps

### Immediate
1. **Build the project**: The IDE needs to refresh and download the Spring Integration dependencies
   ```powershell
   ./gradlew clean build
   ```

2. **Run tests**: Verify the configuration is correct
   ```powershell
   ./gradlew test
   ```

### Future Implementation
1. **Implement Price Fetching Logic**
   - Update `PriceFetcherService.fetchPrices()` to call external APIs
   - Consider using the existing Alpha Vantage client under `alphavantage.api` package
   - Add error handling and retry logic

2. **Add Data Persistence**
   - Store fetched prices in the database (price table already exists from V1 migration)
   - Use jOOQ for database operations (already configured in project)
   - Update the service activator to persist data

3. **Error Handling**
   - Add exception handling in the flow
   - Configure error channels
   - Implement retry logic for failed API calls

4. **Monitoring & Metrics**
   - Add Spring Boot Actuator metrics
   - Create custom metrics for successful/failed fetches
   - Add health indicators for the integration flow

5. **Holiday Calendar Integration**
   - Add logic to skip execution on market holidays
   - Configure NYSE/NASDAQ holiday calendar
   - Update message source to check holiday calendar

6. **Integration Testing**
   - Create integration tests in `src/integrationTest/java`
   - Use Testcontainers for database interactions
   - Mock external API calls

## Configuration Options

### Enable/Disable the Flow
Set in `application.properties`:
```properties
price-fetcher.enabled=false  # Disable the flow
price-fetcher.enabled=true   # Enable the flow (default)
```

### Override for Testing
You can create `application-test.properties`:
```properties
price-fetcher.enabled=false
```

Then run tests with:
```powershell
./gradlew test -Dspring.profiles.active=test
```

## Troubleshooting

### Flow Not Triggering
1. Check logs for "Price fetcher triggered at:" messages
2. Verify `price-fetcher.enabled=true` in application.properties
3. Check that Spring Integration is enabled (@EnableIntegration is present)

### Build Errors
1. Refresh Gradle dependencies: `./gradlew --refresh-dependencies`
2. Clean and rebuild: `./gradlew clean build`
3. Check IDE has indexed the new dependencies

### Timezone Issues
- The flow uses `America/New_York` timezone explicitly
- If server is in different timezone, the flow will still trigger at correct EST/EDT times
- Logs will show timestamps in server's local timezone, but trigger times are EST/EDT

## References
- [Spring Integration Documentation](https://docs.spring.io/spring-integration/reference/)
- [Spring Integration DSL](https://docs.spring.io/spring-integration/reference/dsl.html)
- [Cron Expression Guide](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/support/CronExpression.html)

## Contact
For questions or issues, refer to the project's main README.md or contact the package owner.

