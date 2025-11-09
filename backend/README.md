## Migration Tool

### Run Instructions
- `Backend`: `mvn spring-boot:run` from the project root.
- API is available at `http://localhost:5000` (default server port configured by Spring Boot). Swagger UI: `http://localhost:5000/swagger-ui/index.html`.
- Adjust `application.yaml` if you need a different port.

### Technical Decisions
- `Spring Boot` powers the REST API for rapid setup and dependency injection.
- `ConcurrentHashMap` stores legacy and migrated clients in-memory for demo simplicity.
- `ClientService` seeds example data at startup; this simulates a legacy source.
- `Lombok` reduces boilerplate in the `Client` model (`@Data`, constructors).
- `springdoc-openapi` exposes Swagger UI for endpoint discovery/testing.
- `ClientMigrationController` centralises the REST endpoints and enables CORS (`@CrossOrigin("*")`) so the Vue dev server can call the API during development.
- `ClientService` encapsulates domain behaviour (lookup, migration, logging) so controllers stay thin and a persistence layer could be added later.
- Logging via `ClientService` (`log.info`) confirms successful migrations without polluting controllers.
- Error responses are wrapped in a structured record (status, error, message, path, timestamp) returned with the correct HTTP status for easy debugging on the frontend.
- In-memory storage keeps the exercise fast to run while mimicking separation between “legacy” and “new” stores; swapping in a database would only require replacing the service storage layer.

