# Price Fetcher Integration Flow

## Overview
This package contains a Spring Integration flow that triggers price fetching at predetermined times during market hours.

## Schedule
The price fetcher runs every 3 hours between 9 AM EST and 5 PM EST:
- 9:00 AM EST
- 12:00 PM EST (Noon)
- 3:00 PM EST

The flow uses a cron trigger configured with Eastern Time Zone to ensure consistent execution regardless of server timezone.

## Components

### PriceFetcherIntegrationConfig
Main configuration class that defines the Spring Integration flow.

**Key Beans:**
- `priceFetcherInputChannel()` - Input channel for the flow
- `priceFetcherOutputChannel()` - Output channel for the flow
- `priceFetcherMessageSource()` - Message source that generates trigger messages with current timestamp
- `priceFetcherPoller()` - Poller with cron trigger (`0 0 9,12,15 * * ?` in EST)
- `priceFetcherFlow()` - The integration flow definition
- `handlePriceFetchComplete()` - Service activator for post-processing

## How It Works

1. **Trigger**: The cron trigger fires at the scheduled times (9 AM, 12 PM, 3 PM EST)
2. **Message Generation**: The message source creates a message containing the current `ZonedDateTime`
3. **Processing**: The flow processes the message through the input channel
4. **Handler**: A simple handler logs the request (TODO: add actual price fetching logic)
5. **Output**: The message is sent to the output channel
6. **Post-Processing**: The service activator handles the completed fetch (TODO: add persistence/notification logic)

## Configuration

### Cron Expression
```
0 0 9,12,15 * * ?
```
- Format: `second minute hour day-of-month month day-of-week`
- `0 0` - At the start of the hour (0 seconds, 0 minutes)
- `9,12,15` - At hours 9, 12, and 15 (9 AM, 12 PM, 3 PM)
- `* * ?` - Every day of every month, day of week ignored

### Timezone
All times are in **America/New_York (EST/EDT)** timezone to align with US market hours.

## Dependencies
This flow requires the following Spring Boot starters:
- `spring-boot-starter-integration`
- `spring-integration-core`

## Testing
Unit tests are provided in `PriceFetcherIntegrationConfigTest` to verify:
- Context loads successfully
- All beans are created
- Message source can generate messages
- Poller is configured with cron trigger

## Future Enhancements
- [ ] Implement actual price fetching logic in the flow handler
- [ ] Add error handling and retry logic
- [ ] Implement persistence of fetched prices
- [ ] Add metrics and monitoring
- [ ] Configure conditional execution (e.g., skip on holidays)
- [ ] Add integration tests with Testcontainers

## Usage

The flow starts automatically when the Spring Boot application starts. No manual intervention is required.

To verify the flow is running, check the logs:
```
Price fetcher triggered at: [timestamp]
Processing price fetch request: [timestamp]
Price fetch completed at: [timestamp]
```

## Notes
- The last run at 3 PM EST is within the 9 AM - 5 PM window (ends at 6 PM when the next 3-hour interval would occur)
- Market hours are typically 9:30 AM - 4 PM EST, so all scheduled runs fall within extended market hours
- Consider adding a check to skip execution on market holidays

