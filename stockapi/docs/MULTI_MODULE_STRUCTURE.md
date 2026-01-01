# Multi-Module Gradle Project Structure

This project has been converted to a multi-module Gradle project.

## Structure

```
musicapi/ (root)
├── build.gradle              # Parent build configuration
├── settings.gradle           # Module definitions
├── gradle.properties         # Shared properties
├── gradlew / gradlew.bat    # Gradle wrapper scripts
└── stockapi/                 # Main application module
    ├── build.gradle          # Module-specific build configuration
    └── src/
        ├── main/
        │   ├── java/
        │   └── resources/
        ├── test/
        └── integrationTest/
```

## Modules

### stockapi
The main Spring Boot application module containing:
- Stock quote API endpoints
- REST clients for external APIs (StockData, AlphaVantage)
- Database migrations (Flyway)
- jOOQ code generation
- Integration tests using Testcontainers

## Running Gradle Tasks

For multi-module projects, you can run tasks at the root level or target specific modules:

### Root level (runs on all modules)
```powershell
./gradlew.bat clean build
./gradlew.bat test
```

### Module-specific tasks
```powershell
# Run the Spring Boot application
./gradlew.bat :stockapi:bootRun

# Build only the stockapi module
./gradlew.bat :stockapi:build

# Run tests for stockapi module
./gradlew.bat :stockapi:test

# Generate jOOQ classes for stockapi
./gradlew.bat :stockapi:generateJooq -PgenerateJooq=true
```

## Adding New Modules

To add a new module to this project:

1. Create a new directory at the root level (e.g., `new-module/`)
2. Add `include 'new-module'` to `settings.gradle`
3. Create a `build.gradle` in the new module directory
4. Apply necessary plugins and configure dependencies

Example module `build.gradle`:
```groovy
plugins {
    id 'java'
    id 'org.springframework.boot'
    id 'io.spring.dependency-management'
}

dependencies {
    // Add dependencies here
    // To depend on stockapi: implementation project(':stockapi')
}
```

## Benefits of Multi-Module Structure

- **Modularity**: Separation of concerns with distinct modules
- **Reusability**: Modules can be shared across projects
- **Build optimization**: Gradle can cache and parallelize module builds
- **Dependency management**: Clear dependency boundaries between modules
- **Future extensibility**: Easy to add new modules (e.g., separate API clients, shared libraries)

## Migration Notes

All existing code has been moved to the `stockapi` module:
- Source code: `stockapi/src/main/java/`
- Resources: `stockapi/src/main/resources/`
- Tests: `stockapi/src/test/java/`
- Integration tests: `stockapi/src/integrationTest/java/`

Build configuration specific to the stockapi module (jOOQ, Flyway, Testcontainers) is now in `stockapi/build.gradle`.

Shared configuration (plugin versions, Java toolchain) is in the root `build.gradle`.

