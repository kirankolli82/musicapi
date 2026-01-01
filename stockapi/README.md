# stockapi Module

A Spring Boot REST API that fetches stock quote data from external providers and exposes HTTP endpoints for querying financial data.

## Overview

The stockapi module is the main application module of the musicapi project. It provides:
- REST endpoints for stock quotes and financial data
- Integration with external stock-data providers (StockData.org, AlphaVantage)
- Secure credential management using Google Cloud Secret Manager
- PostgreSQL database with Flyway migrations
- Type-safe database access using jOOQ
- Comprehensive testing with Testcontainers

## Features

### REST API Endpoints

#### GET /quotes
Returns stock quotes for configured ticker symbols.

**Response Example:**
```json
{
  "meta": {
    "requested": "2026-01-01T10:00:00Z",
    "returned": 3
  },
  "data": {
    "NVDA": {
      "ticker": "NVDA",
      "price": 420.12,
      "volume": 123456,
      "timestamp": "2026-01-01T10:00:00Z"
    },
    "MSFT": { ... },
    "AAPL": { ... }
  }
}
```

Default symbols: `NVDA`, `MSFT`, `AAPL`

### External API Integrations

- **StockData.org API** - Primary stock quote provider
- **AlphaVantage API** - Alternative data source
- **Google Cloud Secret Manager** - Secure API token storage

### Database

- **Database:** PostgreSQL
- **Migrations:** Flyway (SQL-based)
- **Code Generation:** jOOQ for type-safe SQL queries
- **Schema:** `refdata` schema with price and quote tables

### Testing

- **Unit Tests:** JUnit 5 in `src/test/java`
- **Integration Tests:** Testcontainers-based tests in `src/integrationTest/java`
- **Test Coverage:** PostgreSQL and Kafka containers

## Getting Started

### Prerequisites

- Java 11 or higher
- Docker (for Testcontainers and local PostgreSQL)
- Google Cloud SDK (optional, for Secret Manager)

### Quick Start

1. **Run the application:**
   ```powershell
   ./gradlew.bat :stockapi:bootRun
   ```

2. **Test the API:**
   ```powershell
   curl http://localhost:8888/quotes
   ```

3. **Run tests:**
   ```powershell
   ./gradlew.bat :stockapi:test
   ./gradlew.bat :stockapi:integrationTest
   ```

### Configuration

Application configuration is in `src/main/resources/application.properties`.

**Key Properties:**
```properties
# Server
server.port=8888

# External API
quote-client.base-url=https://api.stockdata.org/v1
quote-client.gcp-project-id=${GCP_PROJECT_ID}
quote-client.api-token-secret-id=${API_TOKEN_SECRET_ID:stockdata-api-token}

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/stockdb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

**Environment Variables:**
- `GCP_PROJECT_ID` - Your GCP project ID
- `API_TOKEN_SECRET_ID` - Secret Manager secret ID for API token

## Documentation

- 📖 **[Quick Start Guide](docs/QUICKSTART.md)** - Comprehensive getting started guide
- 🔧 **[Multi-Module Structure](docs/MULTI_MODULE_STRUCTURE.md)** - Project structure details
- 🔐 **[Secret Manager Setup](docs/SECRETS_MANAGER_SETUP.md)** - GCP Secret Manager integration guide
- 🐛 **[Troubleshooting Secret Manager](docs/TROUBLESHOOTING_SECRET_MANAGER.md)** - Debug GCP authentication issues
- 📋 **[Migration Checklist](docs/MIGRATION_CHECKLIST.md)** - Multi-module migration history

## Project Structure

```
stockapi/
├── build.gradle                      # Module build configuration
├── README.md                         # This file
├── docs/                             # Module documentation
│   ├── QUICKSTART.md
│   ├── MULTI_MODULE_STRUCTURE.md
│   ├── SECRETS_MANAGER_SETUP.md
│   ├── TROUBLESHOOTING_SECRET_MANAGER.md
│   └── MIGRATION_CHECKLIST.md
├── src/
│   ├── main/
│   │   ├── java/com/kiran/stockapi/
│   │   │   ├── StockApiApplication.java   # Main entry point
│   │   │   ├── alphavantage/api/          # AlphaVantage integration
│   │   │   ├── common/gcp/                # GCP utilities
│   │   │   ├── config/                    # Spring configuration
│   │   │   ├── stockdata/api/             # StockData.org integration
│   │   │   │   ├── client/                # REST clients
│   │   │   │   ├── contract/              # DTOs
│   │   │   │   └── resources/             # REST endpoints
│   │   │   └── utils/                     # Shared utilities
│   │   └── resources/
│   │       ├── application.properties     # Application config
│   │       └── db/migration/              # Flyway SQL migrations
│   ├── test/java/                         # Unit tests
│   └── integrationTest/java/              # Integration tests
└── build/
    └── generated-src/jooq/                # jOOQ generated code
```

## Common Tasks

### Building

```powershell
# Build the module
./gradlew.bat :stockapi:build

# Clean build
./gradlew.bat :stockapi:clean build

# Skip tests
./gradlew.bat :stockapi:build -x test
```

### Running

```powershell
# Run the application
./gradlew.bat :stockapi:bootRun

# Build and run as JAR
./gradlew.bat :stockapi:bootJar
java -jar build/libs/stockapi-*.jar
```

### Testing

```powershell
# Run unit tests
./gradlew.bat :stockapi:test

# Run integration tests
./gradlew.bat :stockapi:integrationTest

# Run all tests
./gradlew.bat :stockapi:check
```

### jOOQ Code Generation

```powershell
# Generate jOOQ classes
./gradlew.bat :stockapi:generateJooq -PgenerateJooq=true

# Generate and compile
./gradlew.bat :stockapi:classes -PgenerateJooq=true
```

**Note:** jOOQ generation starts a PostgreSQL Testcontainer, runs Flyway migrations, and generates type-safe Java classes from the schema.

### Code Formatting

```powershell
# Check formatting (Spotless)
./gradlew.bat :stockapi:spotlessCheck

# Apply formatting
./gradlew.bat :stockapi:spotlessApply
```

## Development

### Package Structure

Following the project's package conventions:

```
com.kiran.stockapi.<feature|datasource>.api
  ├── client/      # REST client classes
  ├── resources/   # REST endpoints (controllers)
  └── contract/    # Request/response DTOs
```

**Example:** StockData.org integration:
- `com.kiran.stockapi.stockdata.api.client.QuoteClient`
- `com.kiran.stockapi.stockdata.api.resources.StockDataResource`
- `com.kiran.stockapi.stockdata.api.contract.StockApiResponse`

### Adding a New Feature

1. Create package under `com.kiran.stockapi.<feature>.api`
2. Add client classes in `<feature>.api.client`
3. Add DTOs in `<feature>.api.contract`
4. Add endpoints in `<feature>.api.resources`
5. Write unit tests in `src/test/java`
6. Write integration tests in `src/integrationTest/java` (if needed)

### Database Migrations

Add new migrations to `src/main/resources/db/migration/`:
```
V1__create_refdata_schema_and_price_table.sql
V2__add_new_table.sql
V3__add_index.sql
```

Naming convention: `V<version>__<description>.sql`

After adding a migration:
1. Re-generate jOOQ classes: `./gradlew.bat :stockapi:generateJooq -PgenerateJooq=true`
2. Update repository/service classes to use new schema

## Technology Stack

- **Framework:** Spring Boot 2.7.x
- **Build Tool:** Gradle 8.x
- **Database:** PostgreSQL 14+
- **Migration:** Flyway
- **SQL DSL:** jOOQ
- **Testing:** JUnit 5, Testcontainers
- **Cloud:** Google Cloud Secret Manager
- **Code Quality:** Spotless (Google Java Style)
- **Logging:** SLF4J + Logback

## Security

### API Token Management

The module uses Google Cloud Secret Manager for secure credential storage:
- API tokens are never stored in code or configuration files
- Tokens are cached in-memory for performance
- Cache auto-evicts every 12 hours to support rotation

See [Secret Manager Setup](docs/SECRETS_MANAGER_SETUP.md) for configuration details.

### Local Development

For local development without GCP:
1. Authenticate: `gcloud auth application-default login`
2. Set environment variables: `GCP_PROJECT_ID`, `API_TOKEN_SECRET_ID`
3. Enable diagnostics: `gcp.diagnostics.enabled=true`

## Troubleshooting

### Common Issues

**Application won't start:**
- Check database connection in `application.properties`
- Verify Docker is running (for Testcontainers)
- Check logs for detailed error messages

**Secret Manager errors:**
- See [Troubleshooting Secret Manager](docs/TROUBLESHOOTING_SECRET_MANAGER.md)
- Enable diagnostics: `gcp.diagnostics.enabled=true`
- Verify authentication: `gcloud auth application-default login`

**jOOQ generation fails:**
- Stop Gradle daemon: `./gradlew.bat --stop`
- Run with debug: `./gradlew.bat :stockapi:generateJooq -PgenerateJooq=true -i`
- Check PostgreSQL JDBC driver is on buildscript classpath

**Tests fail:**
- Ensure Docker is running (Testcontainers needs it)
- Clear Gradle cache: `./gradlew.bat clean`
- Run with stacktrace: `./gradlew.bat :stockapi:test --stacktrace`

## Contributing

- Follow Google Java Style Guide
- See `.github/copilot-instructions.md` for coding conventions
- Write tests for new features
- Update documentation when adding endpoints or features
- Keep commits small and focused

## Resources

- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **jOOQ Manual:** https://www.jooq.org/doc/latest/manual/
- **Flyway Docs:** https://flywaydb.org/documentation/
- **Testcontainers:** https://www.testcontainers.org/

---

**Package:** `com.kiran.stockapi`  
**Module Type:** Spring Boot Application  
**Parent Project:** [musicapi](../README.md)

