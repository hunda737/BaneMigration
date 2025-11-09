# Migration Tool

This project contains both the Spring Boot backend and the Vue/Vuetify frontend for the client migration demo.

## Backend (`backend/`)

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

## Frontend (`frontend/`)

### Getting Started
- **Prerequisites**
  - Node.js 20+ (template built with Vite/Vue tooling; upgrade if npm warns about engines)
  - Backend API running at `http://localhost:5000`
- **Install dependencies**
  - `npm install`
- **Run the development server**
  - `npm run dev`
  - Open the printed local URL (default: `http://localhost:5173`)
  - Ensure the Spring Boot API is reachable; the app expects endpoints under `/api`
- **Build for production**
  - `npm run build`

### Technical Choices
- **Vite + Vue 3**
  - Modern, fast dev experience with native ES modules and minimal configuration
- **Vuetify**
  - Provides accessible, ready-to-use Material Design components (data tables, buttons, layout)
- **Tailwind CSS**
  - Utility-first styling for quick layout tweaks and consistent spacing alongside Vuetify themes
- **Fetch-based API service**
  - Centralized `apiService` wraps the backend calls (`GET /legacy/clients`, `POST /migrate/:id`, `GET /new/clients`) with error handling and logging
- **Feedback & state management**
  - Vue `ref` state, Vuetify snackbar, and shared loading state keep the user informed during migrations

