# Quick Start Guide - Multi-Module Project

This project is now a multi-module Gradle project with the main application in the `stockapi` module.

## 🚀 Getting Started

### First Time Setup

1. **Clone the repository** (if not already done)
   ```powershell
   git clone <repository-url>
   cd musicapi
   ```

2. **Verify the project structure**
   ```powershell
   ./gradlew.bat projects
   ```
   
   You should see:
   ```
   Root project 'musicapi'
   \--- Project ':stockapi'
   ```

3. **Build the project**
   ```powershell
   ./gradlew.bat build -x generateJooq
   ```

### Running the Application

```powershell
# Run the Spring Boot application
./gradlew.bat :stockapi:bootRun
```

The application will start on port 8888 (or as configured in `stockapi/src/main/resources/application.properties`).

## 🔧 Common Tasks

### Building

```powershell
# Build all modules
./gradlew.bat build

# Build only stockapi
./gradlew.bat :stockapi:build

# Clean build
./gradlew.bat clean build
```

### Testing

```powershell
# Run all tests
./gradlew.bat test

# Run only stockapi tests
./gradlew.bat :stockapi:test

# Run integration tests
./gradlew.bat :stockapi:integrationTest
```

### jOOQ Code Generation

```powershell
# Generate jOOQ classes
./gradlew.bat :stockapi:generateJooq -PgenerateJooq=true

# Generate and compile
./gradlew.bat :stockapi:classes -PgenerateJooq=true
```

### Code Formatting

```powershell
# Check formatting
./gradlew.bat :stockapi:spotlessCheck

# Apply formatting
./gradlew.bat :stockapi:spotlessApply
```

## 📂 Project Structure

```
musicapi/                           # Root directory
├── build.gradle                    # Parent build configuration
├── settings.gradle                 # Defines modules
├── gradle.properties               # Shared properties
├── README.md                       # Main documentation
├── MULTI_MODULE_STRUCTURE.md      # Multi-module info
└── stockapi/                       # Main application module
    ├── build.gradle                # Module build configuration
    └── src/
        ├── main/
        │   ├── java/               # Application source code
        │   └── resources/          # Configuration, DB migrations
        ├── test/                   # Unit tests
        └── integrationTest/        # Integration tests
```

## 🎯 Key Files

- **Application Entry Point**: `stockapi/src/main/java/com/kiran/stockapi/MusicapiApplication.java`
- **Configuration**: `stockapi/src/main/resources/application.properties`
- **DB Migrations**: `stockapi/src/main/resources/db/migration/`
- **Module Build**: `stockapi/build.gradle`
- **Root Build**: `build.gradle`

## 🔍 Module Reference

When running Gradle tasks, you can:

1. **Run from root** (applies to all modules):
   ```powershell
   ./gradlew.bat test
   ```

2. **Target specific module**:
   ```powershell
   ./gradlew.bat :stockapi:test
   ```

Module names are prefixed with `:` (e.g., `:stockapi`)

## 📖 Additional Documentation

- **README.md** - Main project documentation
- **MULTI_MODULE_STRUCTURE.md** - Detailed multi-module information
- **MIGRATION_CHECKLIST.md** - Changes made during migration
- **SECRETS_MANAGER_SETUP.md** - GCP Secret Manager setup
- **TROUBLESHOOTING_SECRET_MANAGER.md** - Secret Manager troubleshooting

## 💡 Tips

- Use `:stockapi:` prefix for module-specific tasks
- Root-level tasks run on all modules (useful for `clean`, `build`, `test`)
- IDE should automatically detect modules after Gradle refresh
- Generated jOOQ code is in `stockapi/build/generated-src/jooq/`

## 🆘 Need Help?

- Check **[TROUBLESHOOTING_SECRET_MANAGER.md](TROUBLESHOOTING_SECRET_MANAGER.md)** for GCP issues
- Check **[MIGRATION_CHECKLIST.md](MIGRATION_CHECKLIST.md)** for migration-related questions
- Run `./gradlew.bat :stockapi:tasks` to see all available tasks
- Run `./gradlew.bat help --task <taskname>` for task details

