# Contributing to KrishMart

## Run locally
1. Install JDK 17 and Maven 3.9+.
2. Clone the repository and enter the project folder.
3. Run `mvn -B clean verify`.
4. Run `mvn clean package` and deploy `target/krishmart.war` to Tomcat 9.
5. Open `http://localhost:8080/krishmart/products` (or `/products` if deployed as ROOT).

## Change checklist
- Keep SQL in DAO classes and use `PreparedStatement`.
- Add/update tests for changed behavior.
- Add a numbered migration for schema changes.
- Update README and CHANGELOG.
- Never commit credentials or production database files.
