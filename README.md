# ⚡ PowerCut Predictor

Community-powered power cut prediction for Chennai.
Reports from users are analysed to predict when and
where power cuts are likely to happen next.

## Features
- Submit power cut reports for your area
- Get real-time risk predictions
- Live heatmap showing risk across Chennai
- Pattern charts showing historical trends
- Admin dashboard for managing reports
- Email alerts when risk is high

## How to Run Locally

### Prerequisites
- Java 17
- IntelliJ IDEA
- Maven

### Steps
1. Clone the repo
2. Open in IntelliJ IDEA
3. Add your OpenWeatherMap API key in
   application.properties
4. Click the green play button
5. Open http://localhost:8080

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/areas | List all areas |
| POST | /api/reports | Submit outage report |
| GET | /api/reports?area=X | Get reports for area |
| GET | /api/prediction/{area} | Get prediction |
| GET | /api/prediction/all/risk-summary | All risks |
| GET | /api/prediction/patterns/{area} | Patterns |
| POST | /api/auth/login | Get JWT token |

## Pages

| URL | Description |
|-----|-------------|
| / | Homepage |
| /report | Submit outage report |
| /heatmap | Live risk heatmap |
| /patterns | Outage pattern charts |
| /admin | Admin dashboard |
| /swagger-ui/index.html | API docs |

## Tech Stack
- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- H2 Database (dev) / PostgreSQL (prod)
- Thymeleaf
- Spring Security
- JWT Authentication
- OpenWeatherMap API
- Leaflet.js (maps)
- Chart.js (charts)
- Bootstrap 5
- Docker