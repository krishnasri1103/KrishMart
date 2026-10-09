# KrishMart — Capstone Requirements Audit
Based on `R2025_Sem3_JAVA_CapstoneProject.pdf` (Anna University R2025, Semester 3; final review Oct 10, 2026).

## Present in the supplied project (static inspection only)
- Maven WAR project with JDK 17 configuration and `javax.servlet` API suitable for Tomcat 9.
- Layered controller/service/DAO/model/DTO/filter/listener/util/exception packages.
- H2 schema includes users, products, orders, order_items, cart_items, reviews; foreign keys and indexes are defined; monetary fields use DECIMAL(10,2).
- Seed/demo data and bcrypt utility are present.
- Buyer/seller/admin servlet and JSP flows are present in the source.
- Docker multi-stage build compiles a WAR and deploys it to Tomcat 9 as ROOT; `/data` is declared as a volume for the H2 database.
- README, `.gitignore`, and a GitHub Actions Maven verification workflow are present.
- ER, use-case, and place-order sequence diagram source files have been added under `docs/diagrams/`.

## Not proven / must be completed or verified before claiming compliance
The zip was not built or executed in this environment because Maven is not installed here. The GitHub Actions result and a live URL have not been verified.

- **Final-review AI chatbot:** the specification requires a functional widget + server-side `ChatServlet`/`ChatService`, provider abstraction (`gemini|mock`), rate limiting, timeout, caching, and 5–10 FAQ responses. This is not implemented in the supplied source as inspected.
- **Testing:** add/verify JUnit 5 + Mockito tests for DAO CRUD, service rules, servlet/session behavior, and end-to-end register → browse → order → review. Confirm CI passes.
- **Security:** verify bcrypt on all password paths, session ID rotation on login, AuthFilter coverage, escaped output, PreparedStatement-only SQL, safe errors, and no credentials committed.
- **Observability/API:** health endpoint `GET /api/v1/health`, request ID + SLF4J MDC, versioned JSON routes/envelope, field-level validation/status codes.
- **Engineering quality:** Checkstyle and SpotBugs; Javadocs; required pattern documentation (DAO, Front Controller, Singleton, Factory, Strategy, Builder); numbered migrations.
- **Evidence/deliverables:** rendered diagrams in final report and README references, security checklist, manual end-to-end test sheet, 10 concurrent users for 60 seconds load test, final report, slides, demo script/video, public deployment URL, and required commit history (minimum 33 commits per spec).
- **Deployment persistence:** configure a persistent disk mounted at `/data` on the selected host. A container volume declaration alone does not guarantee persistence on a platform with ephemeral storage.

## Immediate verification commands
```bash
mvn -B clean verify
mvn -B clean package
docker build -t krishmart .
docker run --rm -p 8080:8080 -v krishmart-data:/data krishmart
```
For a local Docker smoke test, open `http://localhost:8080/`. The demo seed credentials are documented in README and must be changed before any real use.
