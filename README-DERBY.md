# Java Hot Dog Stand — Derby edition

## Open and run in IntelliJ IDEA

1. Unzip the project to a new folder. Open its `pom.xml` as a Maven project in IntelliJ IDEA.
2. Allow Maven to download Apache Derby 10.17.1.0 and LGoodDatePicker 11.2.1. Internet access is required the first time.
3. Use JDK 17 or newer (your JDK 26 can be selected, subject to library compatibility).
4. Run `program.Main` from IntelliJ. Or run `mvn compile exec:java` from the project directory.

## What changed

- `src/core/DB.java` now uses JDBC directly instead of Flyway.
- On startup it creates `HD_CUSTOMER` and `HD_ORDER` only if absent, preserving existing data.
- `pom.xml` supplies the embedded Apache Derby driver and shared classes, plus the date picker.
- The original SQL migration files are retained as references but are no longer executed by Flyway.

## Database location

The embedded Derby database is created in `hotdogstanddb` relative to the process working directory. To reuse an existing database, run from the same working directory and make a backup before upgrading Derby. Derby database formats can be affected by version upgrades.

## Validation

All project Java source files were successfully compiled using javac with the bundled date-picker JAR. Actual Derby startup and the Swing GUI could not be tested in the build environment because the Derby runtime libraries were not installed there.
