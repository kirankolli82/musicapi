# Price Fetcher Integration Flow - Visual Overview

## Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                    PRICE FETCHER INTEGRATION FLOW                    │
└─────────────────────────────────────────────────────────────────────┘

                            ┌──────────────┐
                            │ Cron Trigger │
                            │ EST Timezone │
                            └──────┬───────┘
                                   │ Fires at:
                                   │ - 9:00 AM EST
                                   │ - 12:00 PM EST
                                   │ - 3:00 PM EST
                                   ▼
                        ┌──────────────────────┐
                        │  Message Source      │
                        │  (if enabled)        │
                        │                      │
                        │ Creates message:     │
                        │ ZonedDateTime.now()  │
                        └──────────┬───────────┘
                                   │
                                   ▼
                        ┌──────────────────────┐
                        │  Input Channel       │
                        │  DirectChannel       │
                        └──────────┬───────────┘
                                   │
                                   ▼
                        ┌──────────────────────┐
                        │  Handler             │
                        │                      │
                        │  1. Log trigger time │
                        │  2. Call service:    │
                        │     fetchPrices()    │
                        └──────────┬───────────┘
                                   │
                                   ▼
                    ┌──────────────────────────────┐
                    │   PriceFetcherService        │
                    │                              │
                    │   TODO: Implement:           │
                    │   - Fetch from API           │
                    │   - Parse response           │
                    │   - Validate data            │
                    │   - Handle errors            │
                    └──────────────┬───────────────┘
                                   │
                                   ▼
                        ┌──────────────────────┐
                        │  Output Channel      │
                        │  DirectChannel       │
                        └──────────┬───────────┘
                                   │
                                   ▼
                        ┌──────────────────────┐
                        │  Service Activator   │
                        │                      │
                        │  - Log completion    │
                        │  TODO:               │
                        │  - Persist to DB     │
                        │  - Send notifications│
                        └──────────────────────┘
```

## Component Relationships

```
PriceFetcherIntegrationConfig
    │
    ├─── priceFetcherPoller() ──────────► PollerMetadata (Cron: 0 0 9,12,15 * * ?)
    │
    ├─── priceFetcherMessageSource() ───► MessageSource<ZonedDateTime>
    │
    ├─── priceFetcherInputChannel() ────► DirectChannel
    │
    ├─── priceFetcherOutputChannel() ───► DirectChannel
    │
    ├─── priceFetcherFlow() ────────────► IntegrationFlow
    │       │
    │       └─── uses ──► PriceFetcherService
    │
    └─── handlePriceFetchComplete() ────► @ServiceActivator
```

## Configuration Properties Flow

```
application.properties
    │
    ├─── price-fetcher.enabled ──────────► @Value in Config ──► Message Source
    │
    ├─── spring.integration.poller.* ────► Default Spring Integration Settings
    │
    └─── [future properties] ────────────► API URLs, tokens, etc.
```

## Execution Timeline (EST)

```
00:00 ─────────────────────────────────────────────────────────────► 24:00
   │                                                                    │
   │     ┌─────┐                  ┌─────┐                  ┌─────┐   │
   │     │ 9AM │                  │12PM │                  │ 3PM │   │
   │     └─────┘                  └─────┘                  └─────┘   │
   │        │                        │                        │       │
   │        ▼                        ▼                        ▼       │
   │     Trigger                  Trigger                  Trigger    │
   │     Fetch                    Fetch                    Fetch      │
   │                                                                   │
   └─── Market Pre-hours ───┤├─── Market Hours ───┤├─── After Hours ─┘
```

## Package Structure

```
com.kiran.stockapi.price.fetcher/
│
├── config/
│   └── PriceFetcherIntegrationConfig.java
│       ├── @Configuration
│       ├── @EnableIntegration
│       └── Defines all beans and flow
│
├── PriceFetcherService.java
│   ├── @Service
│   └── Business logic (to be implemented)
│
└── README.md
    └── Component documentation
```

## Data Flow

```
Trigger Event
    ↓
ZonedDateTime (current time in EST)
    ↓
Message<ZonedDateTime>
    ↓
Handler extracts payload
    ↓
PriceFetcherService.fetchPrices(ZonedDateTime)
    ↓
[Future: API Call → Price Data]
    ↓
[Future: Persist to Database]
    ↓
Completion logged
```

## Error Handling Flow (To Be Implemented)

```
                    ┌──────────────┐
                    │   Handler    │
                    └──────┬───────┘
                           │
                    Try {  │  } Catch
                           │
              ┌────────────┴────────────┐
              ▼                         ▼
     ┌─────────────────┐      ┌─────────────────┐
     │  Success Path   │      │   Error Path    │
     │                 │      │                 │
     │  - Log success  │      │  - Log error    │
     │  - Continue     │      │  - Retry logic  │
     │  - Persist      │      │  - Error channel│
     └─────────────────┘      └─────────────────┘
```
```
        └── Schedule verification
        ├── API mocking
        ├── Database persistence
    └── Full flow test with Testcontainers
    │
Integration Tests (Future)

        └── Tests bean creation and configuration
    └── PriceFetcherIntegrationConfigTest
    │
    │   └── Tests service methods in isolation
    ├── PriceFetcherServiceTest
    │
Unit Tests
```

## Testing Strategy

5. **Placeholder Implementation** - Ready for real implementation
4. **Separate Service** - Business logic separate from flow config
3. **Enabled Flag** - Can disable via properties for testing
2. **EST Timezone** - Explicit timezone for market hours alignment
1. **Java Configuration Only** - No XML, all configuration in code

## Key Design Decisions


