# musicapi

A multi-module Gradle project for building financial data APIs with Spring Boot.

## Project Overview

This is a multi-module Gradle project with the following structure:
- **Root directory** - Shared build configuration and orchestration
- **`stockapi/`** - Main application module (Spring Boot stock quotes API)

## Quick Start

### Running the Application
```powershell
# Run the stock API application
./gradlew.bat :stockapi:bootRun
```

The application will start on port 8888 (configurable in `application.properties`).

### Common Commands
```powershell
# Build all modules
./gradlew.bat build

# Run tests
./gradlew.bat test

# Run integration tests
./gradlew.bat :stockapi:integrationTest

# Generate jOOQ classes
./gradlew.bat :stockapi:classes -PgenerateJooq=true
```

## Modules

### stockapi
A Spring Boot REST API that fetches stock quote data and exposes HTTP endpoints.

**Key Features:**
- REST endpoint for stock quotes (`GET /quotes`)
- Integration with external stock-data providers (StockData.org, AlphaVantage)
- Google Cloud Secret Manager integration for secure credential storage
- PostgreSQL with Flyway migrations and jOOQ code generation
- Testcontainers for integration testing

**Documentation:**
- 📖 [Quick Start Guide](stockapi/docs/QUICKSTART.md) - Get started with the stockapi module
- 🔧 [Multi-Module Structure](stockapi/docs/MULTI_MODULE_STRUCTURE.md) - Understanding the project structure
- 🔐 [Secret Manager Setup](stockapi/docs/SECRETS_MANAGER_SETUP.md) - GCP Secret Manager integration
- 🐛 [Troubleshooting Secret Manager](stockapi/docs/TROUBLESHOOTING_SECRET_MANAGER.md) - Debug GCP issues
- 📋 [Migration Checklist](stockapi/docs/MIGRATION_CHECKLIST.md) - Multi-module migration details

## Project Structure

```
musicapi/
├── build.gradle              # Parent build configuration
├── settings.gradle           # Module definitions
├── gradle.properties         # Shared properties
├── README.md                 # This file
└── stockapi/                 # Stock API module
    ├── build.gradle          # Module build configuration
    ├── docs/                 # Module documentation
    └── src/
        ├── main/             # Application code
        ├── test/             # Unit tests
        └── integrationTest/  # Integration tests
```

## Development Workflow

### Working with Modules

This is a multi-module Gradle project. You can run tasks at the root level (applies to all modules) or target specific modules:

```powershell
# Root level - runs on all modules
./gradlew.bat clean build

# Module-specific - prefix with :moduleName:
./gradlew.bat :stockapi:bootRun
./gradlew.bat :stockapi:test
```

See [Multi-Module Structure](stockapi/docs/MULTI_MODULE_STRUCTURE.md) for detailed information.

### Adding New Modules

To add a new module:
1. Create a directory at the root level (e.g., `newmodule/`)
2. Add `include 'newmodule'` to `settings.gradle`
3. Create a `build.gradle` in the module directory
4. Apply necessary plugins and configure dependencies

## Technology Stack

- **Language:** Java (Gradle build)
- **Framework:** Spring Boot
- **Database:** PostgreSQL with Flyway migrations
- **Code Generation:** jOOQ for type-safe SQL
- **Testing:** JUnit 5, Testcontainers
- **Cloud Integration:** Google Cloud Secret Manager
- **Package Management:** Gradle with multi-module support

## Getting Help

- **General questions:** See the [Quick Start Guide](stockapi/docs/QUICKSTART.md)
- **Build issues:** Check [Multi-Module Structure](stockapi/docs/MULTI_MODULE_STRUCTURE.md)
- **Secret Manager errors:** See [Troubleshooting Guide](stockapi/docs/TROUBLESHOOTING_SECRET_MANAGER.md)
- **Available tasks:** Run `./gradlew.bat :stockapi:tasks`

## Contributing

- Follow the Google Java Style Guide
- See `.github/copilot-instructions.md` for coding conventions
- Keep changes focused and include tests
- Update documentation when adding features

---

**Main Package:** `com.kiran.stockapi`  
**Documentation:** See `stockapi/docs/` for module-specific guides
