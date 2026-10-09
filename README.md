# KrishMart

KrishMart is a layered, multi-seller marketplace built for JDK 17 and Tomcat 9. Sellers publish listings, buyers search and purchase products with a mock checkout, and administrators moderate listings and manage order status.

## Tech stack

| Layer | Technology |
|---|---|
| Runtime | JDK 17, Apache Tomcat 9.0.x |
| Build | Maven, WAR packaging |
| Web | `javax.servlet.*`, JSP, JSTL, vanilla JavaScript/fetch |
| Data | H2 embedded for development/tests; H2 server URL supported for deployment |
| Persistence | JDBC DAO layer with HikariCP |
| JSON/security | Gson, jBCrypt |
| Quality | JUnit 5, Mockito, SLF4J, Logback |

## Features

- Buyer and seller registration/login; admin exists only through seed data.
- Seller listing create, edit, delete with image URL, price, stock, and category.
- Catalog keyword/category search.
- Buyer cart with add/update/remove and decimal running totals.
- Mock payment confirmation and transactional checkout with stock protection.
- Buyer order history and seller incoming orders.
- Admin user/order/listing dashboard plus status workflow controls.
- Product reviews are accepted only from buyers with a delivered purchase.
- Auth and UTF-8 filters, bcrypt passwords, prepared statements, escaped JSTL output, safe error page, and explicit 30-minute session timeout.

## Demo accounts

The idempotent seed creates:

| Role | Email | Password |
|---|---|---|
| Admin | `admin@krishmart.local` | `password` |
| Seller | `seller@krishmart.local` | `password` |
| Buyer | `buyer@krishmart.local` | `password` |

Change or replace seed credentials before using this outside a demo environment.

## Capstone status and known gaps\n\nThe base marketplace includes the core buyer/seller/admin flows described above. Before claiming full capstone compliance, verify these remaining deliverables: AI chatbot provider and widget (required for Final Review), JUnit/Mockito coverage for each layer, health endpoint and request-ID logging, Checkstyle/SpotBugs, numbered migration files, a manual security/end-to-end test sheet, 10-user/60-second load test evidence, and the final report/slide deck/demo recording. Diagram source files are in `docs/diagrams/`; include rendered diagrams in the final report. The GitHub Actions workflow runs Maven verification but passing CI must be confirmed on GitHub.\n\n## Run locally

1. Install JDK 17 and Maven.
2. Copy `src/main/resources/config.properties.example` to `src/main/resources/config.properties` if you need custom settings. The latter is ignored by Git. Defaults use an embedded local database at `./data/krishmart`.
3. Run the tests:

   ```bash
   mvn clean test
   ```

4. Build the deployable WAR:

   ```bash
   mvn clean package
   ```

5. Copy `target/krishmart.war` to Tomcat 9's `webapps/` directory and start Tomcat. Open `http://localhost:8080/krishmart/products`.

For a deployment H2 server, set `KRISHMART_DB_URL`, `KRISHMART_DB_USERNAME`, and `KRISHMART_DB_PASSWORD` as environment variables. No database credentials are committed.

## Project layout

`controller` contains HTTP orchestration only; `service` contains validation and business rules; `dao` contains every SQL statement; `model` contains entities; `dto` contains request/response shapes; `filter`, `listener`, `util`, and `exception` provide infrastructure and safe cross-cutting behavior.

The H2 schema is in `src/main/resources/schema.sql`, and idempotent development data is in `seed.sql`. The listener owns creation and shutdown of the HikariCP pool and applies both scripts at startup.