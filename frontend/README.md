# Bane Client Migration Frontend

## Getting Started

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

## Technical Choices

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
