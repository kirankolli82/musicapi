# AlphaVantage API Integration

## Overview
This implementation provides a REST client for the AlphaVantage API, following the same architectural pattern as the existing `stockdata` API integration.

## Implementation Details

### Package Structure
Following the project's coding instructions, the implementation is organized as:

```
com.kiran.stockapi.alphavantage.api
├── client/
│   ├── AlphaVantageClient.java              (REST client interface)
│   └── AlphaVantageClientRequestFilter.java (Request filter for API key injection)
├── config/
│   ├── AlphaVantageClientProperties.java    (Configuration properties)
│   └── AlphaVantageConfig.java              (Spring configuration bean)
├── contract/
│   ├── RealtimeBulkQuotesResponse.java      (Response DTO - already existed)
│   └── StockQuote.java                      (Stock quote DTO - already existed)
└── resources/
    └── AlphaVantageResource.java            (REST endpoint controller)
```

### Key Components

#### 1. AlphaVantageClient (REST Client Interface)
- JAX-RS annotated interface for calling AlphaVantage API
- Method: `getRealtimeBulkQuotes(String function, String symbols)`
- Path: `/query`
- Query parameters: `function` and `symbol`

#### 2. AlphaVantageClientRequestFilter
- Implements `ClientRequestFilter` to inject API key into every request
- Fetches API key from GCP Secret Manager using `SecretManagerService`
- Adds `apikey` query parameter to the request URI
- Secret name: `alpha_vantage_access_key`

#### 3. AlphaVantageClientProperties
- Spring Boot configuration properties record
- Prefix: `alphavantage-client`
- Properties:
  - `baseUrl`: Base URL for AlphaVantage API
  - `gcpProjectId`: GCP project ID for Secret Manager
  - `apiKeySecretId`: Secret ID for API key in GCP Secret Manager

#### 4. AlphaVantageConfig
- Spring `@Configuration` class
- Creates and configures the `AlphaVantageClient` bean
- Registers the request filter for automatic API key injection
- Uses RESTEasy JAX-RS client implementation

#### 5. AlphaVantageResource
- REST controller exposing AlphaVantage functionality
- Endpoint: `GET /alphavantage/realtime-bulk-quotes`
- Query parameter: `symbols` (default: "GRID,MSFT,AAPL,IBM")
- Uses SLF4J for logging

### Configuration

Add the following to `application.properties`:

```properties
alphavantage-client.base-url=https://www.alphavantage.co
alphavantage-client.gcp-project-id=${GCP_PROJECT_ID:kiran-stock-api-project}
alphavantage-client.api-key-secret-id=${ALPHAVANTAGE_API_KEY_SECRET_ID:alpha_vantage_access_key}
```

### GCP Secret Manager Setup

The API key must be stored in GCP Secret Manager with:
- Secret name: `alpha_vantage_access_key`
- Project: as specified in `alphavantage-client.gcp-project-id`

### API Usage Example

The AlphaVantage API endpoint being called is:
```
https://www.alphavantage.co/query?function=REALTIME_BULK_QUOTES&symbol=GRID,MSFT,AAPL,IBM&apikey={API_KEY}
```

The API key is automatically injected by the `AlphaVantageClientRequestFilter`.

### Testing

Unit tests have been created for:
1. `AlphaVantageClientRequestFilterTest` - Tests the request filter logic
2. `AlphaVantageResourceTest` - Tests the REST endpoint controller

Tests are located in:
- `src/test/java/com/kiran/musicapi/alphavantage/api/client/`
- `src/test/java/com/kiran/musicapi/alphavantage/api/resources/`

### Usage

To call the AlphaVantage API through the application:

```bash
# Using default symbols (GRID,MSFT,AAPL,IBM)
curl http://localhost:8888/alphavantage/realtime-bulk-quotes

# Using custom symbols
curl http://localhost:8888/alphavantage/realtime-bulk-quotes?symbols=AAPL,GOOGL,MSFT
```

### Design Patterns Followed

1. **Separation of Concerns**: Client, configuration, contract, and resources are in separate packages
2. **Dependency Injection**: All components use constructor injection
3. **Immutability**: DTOs use Lombok builders and final fields
4. **Configuration Externalization**: No hardcoded values, all configuration from properties
5. **Security**: API key retrieved from GCP Secret Manager, never hardcoded
6. **Logging**: SLF4J for proper logging (no System.out)
7. **Testing**: Unit tests with Mockito for mocking dependencies

### Notes

- The implementation follows Google Java Style Guide
- POJOs with BigDecimal fields use Lombok annotations as per project instructions
- BigDecimal fields are excluded from equals/hashCode with stripped versions included
- The API key parameter name is `apikey` as per AlphaVantage API specification
- The function parameter defaults to `REALTIME_BULK_QUOTES` in the resource layer

### Build and Run

To build and test:
```bash
./gradlew clean build
./gradlew test
```

To run the application:
```bash
./gradlew bootRun
```

Make sure GCP credentials are properly configured before running the application.

