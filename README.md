## ✨ COURSE FINDER API

A Spring Boot application that integrates with **Elasticsearch** to index and search educational course data with rich filters and full-text capabilities.

---

## 🚀 Features

- 🔍 Full-text search on course title & description
- 🎯 Filter by category, type, age, price, and date
- 📊 Pagination and sorting (by date or price)
- 🐳 Dockerized Elasticsearch setup
- 📁 Bulk indexing from `sample-courses.json`

---

## ⚙️ Prerequisites

- Java 17+
- Maven
- Docker & Docker Compose

---


## 📥 Clone the Repository

```bash
git clone <your-repo-url>
cd <project-directory>
```

---

## 🐋 Launch Elasticsearch

Make sure Docker is running. Then start Elasticsearch using the provided Docker Compose file:

```bash
docker-compose up -d
```

Verify it’s running:

```bash
curl http://localhost:9200
```

You should receive a JSON response with cluster info.

---

## 🛠 Build and Run the Application


Build and start the Spring Boot application:

```bash
mvn clean install
mvn spring-boot:run
```

## 📦 Sample Data Format

Place your data file here:

```
src/main/resources/sample-courses.json
```

Each object must have 
```
{
  "id": "string",                  // Unique course identifier
  "title": "string",              // Course title
  "description": "string",        // Detailed course description
  "category": "string",           // Category like "Math", "Art", etc.
  "type": "ONE_TIME | COURSE | CLUB",  // Type of course
  "gradeRange": "string",         // Example: "1st–3rd"
  "minAge": number,               // Minimum eligible age
  "maxAge": number,               // Maximum eligible age
  "price": number,                // Price in decimal
  "nextSessionDate": "ISO 8601 datetime string"  // e.g., "2025-06-10T15:00:00Z"
}
```



## 🔄 Data Indexing

On startup, the app reads `sample-courses.json` and bulk indexes all course objects into the `courses` index.

To verify:

```bash
curl http://localhost:9200/courses/_search?pretty
```

---

## 🔍 Search Endpoint

**URL:**

```
GET /api/search
```

### Query Parameters:

| Name         | Type     | Description                                        |
|--------------|----------|----------------------------------------------------|
| `keyword`    | string   | Search keyword (title + description)               |
| `category`   | string   | Exact match category filter                        |
| `type`       | string   | One of: `COURSE`, `ONE_TIME`, `CLUB`               |
| `minAge`     | int      | Minimum age                                        |
| `maxAge`     | int      | Maximum age                                        |
| `minPrice`   | decimal  | Minimum course price                               |
| `maxPrice`   | decimal  | Maximum course price                               |
| `startDate`  | ISO date | Show courses starting on or after this date        |
| `sort`       | string   | `upcoming` (default), `priceAsc`, `priceDesc`      |
| `page`       | int      | Page number (default = 0)                          |
| `size`       | int      | Page size (default = 10)                           |

### Example:

```bash
curl "http://localhost:8080/api/search?keyword=science&category=Science&minAge=8&maxPrice=1500&sort=priceAsc&page=0&size=5"
```

---

## ✅ Sample Response

```json
{
  "total": 34,
  "courses": [
    {
      "id": "course-001",
      "title": "Fun Science",
      "category": "Science",
      "price": 999.99,
      "nextSessionDate": "2025-07-20T10:00:00Z"
    }
  ]
}
```

---

## 🗂 Project Structure

```
src/main/java/com/yourapp/
├── config/         # Elasticsearch configuration
├── controller/     # REST API controller
├── document/       # CourseDocument model
├── repository/     # Custom ES query repository
├── service/        # Search service logic
├── request/        # Accept query/filter parameters
└── response/       # Return filtered course results
```

---

## 📋 Submission Checklist

- [x] Elasticsearch runs via `docker-compose`
- [x] `sample-courses.json` has at least 50 courses
- [x] Spring Boot indexes data on startup
- [x] `/api/search` supports all filters & sorting
- [x] README includes setup & examples

---

## 🔗 Resources

- 📘 [Spring Data Elasticsearch Docs](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/)
- 🧠 [Elasticsearch Reference](https://www.elastic.co/guide/en/elasticsearch/reference/index.html)

---
