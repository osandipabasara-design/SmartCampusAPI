# Smart Campus API

A RESTful Web Service built with **Java (JAX-RS)** using **Jersey** and the **Grizzly HTTP Server**. This API allows for the management of rooms, sensors, and sensor readings within a campus environment.

---

## 🎥 Video Demonstration

[![Smart Campus API Demo](https://img.youtube.com/vi/hc2m8jOXl1o/0.jpg)](https://youtu.be/hc2m8jOXl1o)

▶️ Watch the full Postman demonstration here: [https://youtu.be/hc2m8jOXl1o](https://youtu.be/hc2m8jOXl1o)

---

## 🏗 API Design Overview

This API follows REST architectural principles to model the physical structure of a Smart Campus. The design is built around three core resources — **Rooms**, **Sensors**, and **Sensor Readings** — which reflect real-world relationships: a campus has rooms, rooms contain sensors, and sensors produce readings over time.

**Key design decisions:**

- **Versioned Base Path**: All endpoints are served under `/api/v1/`, providing a clear versioning strategy that allows future versions to coexist without breaking existing clients.
- **Resource Hierarchy**: Sensor readings are modelled as a sub-resource of sensors (`/sensors/{id}/readings`), reflecting their dependent relationship. This uses the JAX-RS Sub-Resource Locator pattern to keep resource classes focused and manageable.
- **Uniform Interface**: Standard HTTP methods are used consistently — `GET` for retrieval, `POST` for creation, `DELETE` for removal — with appropriate status codes (`201 Created`, `204 No Content`, `404 Not Found`, etc.).
- **JSON throughout**: All request and response bodies use `application/json` via Jackson, enforced with `@Produces` and `@Consumes` annotations.
- **In-Memory Storage**: A static `DataStore` using `ConcurrentHashMap` provides thread-safe, persistence-free storage suitable for development and testing.
- **Resilient Error Handling**: Every anticipated error condition has a dedicated custom exception and mapper, ensuring no raw stack traces are ever exposed to clients.
- **Observability**: A `LoggingFilter` logs every incoming request (method + URI) and every outgoing response (status code) without touching individual resource methods.
- **Discovery Endpoint**: `GET /api/v1/` serves as a navigable entry point, returning API metadata and links to primary resource collections (HATEOAS-inspired).

---

## 🚀 Features

- **Room Management**: Create, retrieve, and delete rooms.
- **Sensor Integration**: Link sensors to specific rooms with referential integrity checks.
- **Data Monitoring**: Track historical readings from sensors with automatic `currentValue` updates.
- **Error Handling**: Custom exception mappers for 409, 422, 403, 500 status codes.
- **In-Memory Storage**: Uses `ConcurrentHashMap` for thread-safe, concurrent data access.
- **Request/Response Logging**: JAX-RS filter logs all API traffic automatically.

---

## 🛠 Technology Stack

- **Language**: Java 17
- **Framework**: Jersey (JAX-RS)
- **Server**: Grizzly HTTP Server
- **JSON Provider**: Jackson
- **Build Tool**: Maven

---

## 🏁 Getting Started

### 1. Prerequisites

- Java JDK 17 or higher
- Apache Maven

### 2. Clone the Repository

```bash
git clone <your-github-repo-url>
cd SmartCampusAPI
```

### 3. Run the Application

```bash
mvn clean compile exec:java
```

The server will start at: `http://localhost:8080/api/v1/`

You will see:
```
==============================================
 Smart Campus API is running!
 Visit: http://localhost:8080/api/v1/rooms
 Press ENTER to stop the server.
==============================================
```

---

## 📡 API Endpoints

Follow this order to test the API correctly (data is in-memory and starts empty on every server start).

### 0. Discovery
| Method | URL | Description |
| :--- | :--- | :--- |
| **GET** | `/api/v1/` | Returns API metadata and resource links |

### 1. Rooms
| Method | URL | Description | JSON Body Example |
| :--- | :--- | :--- | :--- |
| **POST** | `/rooms` | Create a room | `{"id": "R1", "name": "Lab 1", "capacity": 30}` |
| **GET** | `/rooms` | List all rooms | N/A |
| **GET** | `/rooms/{id}` | Get room details | N/A |
| **DELETE** | `/rooms/{id}` | Delete a room (fails with 409 if sensors exist) | N/A |

### 2. Sensors
| Method | URL | Description | JSON Body Example |
| :--- | :--- | :--- | :--- |
| **POST** | `/sensors` | Add a sensor (fails with 422 if roomId invalid) | `{"id": "S1", "type": "Temperature", "roomId": "R1", "status": "ACTIVE"}` |
| **GET** | `/sensors` | List all sensors | N/A |
| **GET** | `/sensors?type=CO2` | Filter sensors by type | N/A |
| **GET** | `/sensors/{id}` | Get sensor details | N/A |

### 3. Sensor Readings
| Method | URL | Description | JSON Body Example |
| :--- | :--- | :--- | :--- |
| **POST** | `/sensors/{id}/readings` | Add a reading (fails with 403 if sensor is MAINTENANCE) | `{"value": 22.5}` |
| **GET** | `/sensors/{id}/readings` | List all readings for a sensor | N/A |

---

## 🧪 Sample curl Commands

> Make sure the server is running before executing these commands.

**1. Create a Room**
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id": "R1", "name": "Computer Lab", "capacity": 30}'
```

**2. Add a Sensor to the Room**
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id": "S1", "type": "Temperature", "roomId": "R1", "status": "ACTIVE"}'
```

**3. Post a Sensor Reading**
```bash
curl -X POST http://localhost:8080/api/v1/sensors/S1/readings \
  -H "Content-Type: application/json" \
  -d '{"value": 22.5}'
```

**4. Get All Sensors Filtered by Type**
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

**5. Attempt to Delete a Room That Still Has Sensors (triggers 409 error)**
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/R1
```

**6. Attempt to Add a Sensor with an Invalid Room ID (triggers 422 error)**
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id": "S2", "type": "CO2", "roomId": "INVALID_ROOM", "status": "ACTIVE"}'
```

**7. Get All Readings for a Sensor**
```bash
curl -X GET http://localhost:8080/api/v1/sensors/S1/readings
```

---

## 📂 Project Structure

```
src/main/java/com/smartcampus/smartcampusapi/
├── SmartCampusAPI.java              # Entry point — starts Grizzly server
├── resource/
│   ├── DiscoveryResource.java       # GET /api/v1/ — API metadata
│   ├── RoomResource.java            # /rooms endpoints
│   ├── SensorResource.java          # /sensors endpoints + sub-resource locator
│   └── SensorReadingResource.java   # /sensors/{id}/readings endpoints
├── model/
│   ├── Room.java
│   ├── Sensor.java
│   └── SensorReading.java
├── storage/
│   └── DataStore.java               # ConcurrentHashMap in-memory store
└── exception/
    ├── RoomNotEmptyException.java
    ├── RoomNotEmptyExceptionMapper.java
    ├── LinkedResourceNotFoundException.java
    ├── LinkedResourceNotFoundMapper.java
    ├── SensorUnavailableException.java
    ├── SensorUnavailableMapper.java
    ├── GlobalExceptionMapper.java
    └── LoggingFilter.java
```

---

## ⚠️ Important Note

This project uses **In-Memory Storage**. All data created via Postman or curl will be **erased** when the Java server is stopped or restarted.

---

## 📝 Conceptual Report

### Part 1 — Service Architecture & Setup

**Q: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance created per request or is it a singleton? How does this affect in-memory data management?**

By default in JAX-RS, a new instance of a resource class is created for every incoming HTTP request. This is known as the per-request lifecycle. The implication of this design is that instance variables within a resource class cannot be used to store shared application state — any data assigned to a field of a resource object will be lost after the request completes, since the object itself is discarded.

This is precisely why this project uses a separate static `DataStore` class backed by `ConcurrentHashMap`. Because the maps are `static final` fields on a class loaded once by the JVM, they exist independently of any resource instance lifecycle. They persist for as long as the server process is running, regardless of how many resource instances are created and destroyed per request.

The use of `ConcurrentHashMap` is also intentional: since a new resource instance per request means multiple threads can execute simultaneously (one per incoming request), the data structures must be thread-safe. `ConcurrentHashMap` allows concurrent reads and writes without external synchronisation, preventing race conditions and data corruption under concurrent load.

---

**Q: Why is HATEOAS considered a hallmark of advanced RESTful design? How does it benefit client developers?**

HATEOAS (Hypermedia as the Engine of Application State) is the principle that API responses should include hyperlinks that tell the client what actions or resources are available next, rather than requiring the client to have prior knowledge of the URL structure. It elevates a REST API from a simple data-fetching mechanism to a self-describing, navigable interface.

The primary benefit for client developers is discoverability — a client can start at a single entry point (in this project, `GET /api/v1/`) and navigate to all resources by following links in the responses, without needing to consult static documentation. This reduces tight coupling between the client and the server's URL structure. If the server changes a URL path, clients that follow links dynamically adapt automatically, whereas clients hardcoded to specific URLs would break. It also reduces onboarding friction for new developers exploring the API for the first time.

---

### Part 2 — Room Management

**Q: When returning a list of rooms, what are the implications of returning only IDs versus returning full room objects?**

Returning only IDs is bandwidth-efficient and fast on the server side, but it forces the client to make N additional requests — one per ID — to retrieve the details it needs. This is known as the N+1 problem and can severely degrade performance when the list is large, especially over high-latency networks.

Returning full room objects increases the size of a single response but eliminates those follow-up requests entirely. For most practical use cases — especially where the client needs to display a list of rooms with their names and capacities — returning the full objects is the better trade-off. It reduces round-trips, simplifies client-side logic, and is more appropriate when the payload size per object is small, as it is here with Room (id, name, capacity, sensorIds). If payload size were a concern at scale, a compromise such as returning a summary object (id + name only) could be used.

---

**Q: Is the DELETE operation idempotent in your implementation? Justify with what happens on repeated requests.**

Yes, the DELETE operation is idempotent in this implementation, as it should be according to the HTTP specification. Idempotency means that sending the same request multiple times produces the same server state as sending it once.

In this implementation, the first `DELETE /rooms/{roomId}` request removes the room from the `DataStore` and returns `204 No Content`. If the same request is sent again, the room no longer exists in the map, so the method returns `404 Not Found`. While the HTTP response code differs between the first and subsequent calls, the server state is identical after each call — the room is absent. The resource is not double-deleted, no data is corrupted, and no side effects are triggered. This is consistent with the standard interpretation of idempotency in REST, which concerns state rather than response codes.

---

### Part 3 — Sensor Operations & Linking

**Q: Explain the technical consequences if a client sends data in a format other than `application/json` to a `@Consumes(APPLICATION_JSON)` endpoint.**

When a resource method is annotated with `@Consumes(MediaType.APPLICATION_JSON)`, JAX-RS uses this to match incoming requests during content negotiation. If a client sends a request with a `Content-Type` of `text/plain` or `application/xml`, the JAX-RS runtime will be unable to find a resource method that declares it can consume that media type. It will return an HTTP `415 Unsupported Media Type` response automatically, before the method body is ever executed.

This is handled entirely by the framework layer, which means no defensive code is needed inside the method itself. The client receives a clear, standardised error code indicating the problem is with the format of its request, not with the data content itself. This separation of concerns is one of the reasons `@Consumes` is preferable to manually inspecting the `Content-Type` header inside the method.

---

**Q: Why is the `@QueryParam` approach for filtering generally considered superior to embedding the filter in the URL path (e.g., `/sensors/type/CO2`)?**

Query parameters are semantically the correct tool for filtering, searching, and sorting a collection, while path segments are meant to identify a specific resource. The URL `/sensors/type/CO2` implies that `type/CO2` is a distinct sub-resource with its own identity, which is conceptually misleading and breaks REST's resource-oriented model.

Using `@QueryParam` also offers greater flexibility: multiple filters can be combined naturally (`/sensors?type=CO2&status=ACTIVE`) without redesigning the URL structure. Query parameters are also optional by nature — if the parameter is absent, the full collection is returned — whereas a path-based approach requires a separate route for the unfiltered case. Additionally, caching infrastructure, API gateways, and documentation tools all treat path parameters and query parameters differently and are designed with the assumption that query parameters are used for filtering.

---

### Part 4 — Deep Nesting with Sub-Resources

**Q: Discuss the architectural benefits of the Sub-Resource Locator pattern compared to defining all nested paths in one large controller class.**

The Sub-Resource Locator pattern allows a parent resource class to delegate responsibility for a sub-path to a dedicated child resource class. In this project, `SensorResource` handles `/sensors` and `/sensors/{id}`, and delegates `/sensors/{id}/readings` to `SensorReadingResource` via a locator method.

The primary benefit is separation of concerns. Each resource class has a single, well-defined responsibility. `SensorResource` manages sensor lifecycle operations; `SensorReadingResource` manages reading history. If all paths were defined in one class, that class would grow to handle unrelated concerns, becoming harder to read, test, and maintain — a violation of the Single Responsibility Principle.

It also improves scalability of the codebase. As the API grows (e.g., adding calibration logs or alert thresholds as sub-resources), new classes can be added without modifying existing ones. The locator method also allows the sub-resource instance to receive context from the parent (in this case, the `sensorId`), enabling clean dependency injection without global state. Overall, it mirrors how large real-world APIs are structured across multiple controllers or handlers.

---

### Part 5 — Advanced Error Handling, Exception Mapping & Logging

**Q: Why is HTTP 422 Unprocessable Entity more semantically accurate than 404 Not Found when a referenced resource ID doesn't exist inside a valid JSON payload?**

HTTP 404 Not Found means the resource identified by the request URL could not be found. In the scenario where a client sends `POST /sensors` with a valid URL but includes a `roomId` in the body that refers to a non-existent room, the URL itself (`/sensors`) is perfectly valid and was found. The problem is not with the endpoint — it is with the semantic content of the request body.

HTTP 422 Unprocessable Entity is specifically designed for this situation: the request is syntactically well-formed (valid JSON, correct `Content-Type`) but fails business logic or referential integrity validation. Using 422 communicates precisely that the server understood the request but could not process it due to a logical error in the data. This gives the client much clearer information — it knows the URL is correct and the JSON structure is valid, but it needs to fix the value of `roomId`. A 404 response would be ambiguous and misleading in this context.

---

**Q: From a cybersecurity standpoint, what risks are associated with exposing internal Java stack traces to API consumers?**

Exposing raw stack traces to external clients is a significant security vulnerability for several reasons.

First, stack traces reveal the internal structure of the application — package names, class names, method names, and line numbers. An attacker can use this information to identify the frameworks and libraries in use, look up known vulnerabilities (CVEs) for those specific versions, and craft targeted exploits.

Second, stack traces can expose file system paths (e.g., where the application is deployed), database query strings or table names (if the exception originated in a data layer), and internal logic flow that reveals how the application processes data. This assists in both reconnaissance and in constructing injection attacks.

Third, detailed error messages reduce the effort required for an attacker to understand why an exploit failed and how to refine it. The `GlobalExceptionMapper` in this project addresses this by catching all unhandled `Throwable` instances and returning only a generic `500 Internal Server Error` message, while logging the full detail server-side where only authorised personnel can access it.

---

**Q: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than inserting `Logger.info()` calls into every resource method?**

Cross-cutting concerns are behaviours that apply uniformly across many or all parts of an application, regardless of the specific business logic involved. Embedding `Logger.info()` calls directly in every resource method violates the DRY (Don't Repeat Yourself) principle — the same boilerplate logging code would be duplicated across every endpoint, creating maintenance overhead and a high risk of inconsistency (some methods may be missed or logged differently).

JAX-RS filters solve this cleanly by executing at the framework level, surrounding every request and response automatically. The `LoggingFilter` in this project implements `ContainerRequestFilter` and `ContainerResponseFilter`, meaning every single API call is logged with zero changes needed to any resource class. If the logging format needs to change in the future, it is updated in exactly one place.

Filters also keep resource methods focused purely on business logic, improving readability and testability. The same principle applies to other cross-cutting concerns like authentication, rate limiting, and CORS headers — all are better handled at the filter level than scattered throughout resource classes.
