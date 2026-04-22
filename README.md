# Smart Campus — Sensor & Room Management API

## Overview

This is a RESTful API built for the University of Westminster 
5COSC022C.2 Client-Server Architectures coursework. It manages 
Rooms and Sensors across a university campus.

Built using JAX-RS (Jersey) on an embedded Grizzly HTTP server. 
All data is stored in-memory using ConcurrentHashMap. 
No database is used.

---

## How to Build and Run

### Prerequisites
- JDK 11 or higher
- NetBeans IDE
- Maven (built into NetBeans)

### Steps

**1. Clone the repository**
git clone https://github.com/YourUsername/SmartCampusAPI.git

**2. Open in NetBeans**
- File → Open Project
- Navigate to the cloned folder → Click Open

**3. Build the project**
- Right-click the project → Clean and Build
- Maven will download all dependencies automatically

**4. Run the server**
- Right-click Main.java → Run File
- Console will show:
Smart Campus API started!
Discovery endpoint: http://localhost:8080/api/v1
Press ENTER to stop the server.

**5. Test the API**
- Open Postman or browser
- Go to: http://localhost:8080/api/v1

---

## Sample curl Commands

### 1. Discovery endpoint
curl http://localhost:8080/api/v1

### 2. Get all rooms
curl http://localhost:8080/api/v1/rooms

### 3. Create a new room
curl -X POST http://localhost:8080/api/v1/rooms 
-H "Content-Type: application/json" 
-d '{"id":"HALL-01","name":"Main Hall","capacity":200}'

### 4. Filter sensors by type
curl http://localhost:8080/api/v1/sensors?type=CO2

### 5. Add a sensor reading
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings 
-H "Content-Type: application/json" 
-d '{"value": 23.7}'

### 6. Delete a room that has sensors (returns 409)
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301

### 7. Create sensor with invalid roomId (returns 422)
curl -X POST __http://localhost:8080/api/v1/sensors 
-H "Content-Type: application/json" 
-d '{"id":"TEMP-999","type":"Temperature","status":"ACTIVE", "currentValue":0,"roomId":"FAKE-ROOM"}'

### 8. Post reading to maintenance sensor (returns 403)
curl -X POST http://localhost:8080/api/v1/sensors/CO2-002/readings 
-H "Content-Type: application/json" 
-d '{"value": 450.0}'

---

## Author
- **Name:** Balasooriya Mudiyanselage Vinuji Jayandee Balasooriya  
- **Student ID:** w2120101 (IIT Id - 20240746)  
- **Module:** 5COSC022C.2 Client-Server Architectures  
- **University:** University of Westminster

## Answers to Coursework Specification Questions

**Part 1: Service Architecture & Setup**

1.1 Project & Application Configuration - JAX-RS Resource Lifecycle

By default, JAX-RS follows a per-request lifecycle, meaning a completely new instance of a resource class is created for every incoming HTTP request and discarded immediately after the response is sent. This makes resource classes stateless by nature, they do not retain any information between requests.
This design decision has a direct and significant impact on how application data must be managed. Because each request gets its own fresh resource object, it is impossible to store persistent data inside the resource class itself, any data written to a field in the resource class would be lost the moment the request ends. To solve this, a singleton DataStore class is used to hold all application data for the entire lifetime of the server. Every resource class accesses this single shared instance using DataStore.getInstance(), which guarantees that all requests read from and write to exactly the same data regardless of which resource object handled them.
Since multiple client requests can arrive simultaneously, thread safety becomes a critical concern. If two requests attempted to write to a standard HashMap at the same time, one write operation could overwrite the other or corrupt the data structure entirely, leading to lost data or unpredictable behaviour. To prevent this, ConcurrentHashMap is used instead of a regular HashMap. ConcurrentHashMap is designed specifically for concurrent access and handles simultaneous reads and writes safely without requiring manual synchronisation blocks, ensuring data integrity across all requests.

1.2 The Discovery Endpoint - HATEOAS

HATEOAS, which stands for Hypermedia as the Engine of Application State, is a core REST constraint that requires API responses to include hypermedia links pointing to related resources and available actions. Rather than expecting clients to know all URLs in advance, the API itself provides navigation guidance within each response.
This approach offers significant benefits to client developers compared to relying on static documentation. With HATEOAS, a client only needs to know the single root URL of the API. From the discovery endpoint, the client receives links to all primary resource collections such as /api/v1/rooms and /api/v1/sensors, and can then navigate the entire API by following those links dynamically. This eliminates the need for clients to hardcode URLs throughout their code, which is a fragile practice that breaks whenever the API structure changes.
From a maintainability perspective, if endpoint URLs are updated in a future version of the API, clients that rely on hypermedia links will continue to work without any modification because they always discover URLs at runtime rather than having them baked in at development time. This makes the API genuinely self-documenting and reduces the coupling between the server and its clients, which is a hallmark of a mature and well-designed RESTful system.


**Part 2: Room Management**

2.1 Room Resource Implementation - Returning IDs vs Full Room Objects

When designing a list endpoint such as GET /api/v1/rooms, there are two common approaches: returning only the IDs of the rooms, or returning the complete room objects including all their fields.
Returning only IDs places a significant burden on the client. To display meaningful information about even a small number of rooms, the client must follow up with an individual GET request for each ID. For example, if the system contains 100 rooms, the client would need to send 101 separate HTTP requests, one to retrieve the list of IDs and then one for each room. This dramatically increases network traffic, adds latency, and puts unnecessary load on both the server and the network.
Returning full room objects in a single response eliminates this problem entirely. The client receives all the data it needs in one request, which minimises latency and reduces server load. Although returning complete objects produces a slightly larger response payload, the difference is negligible for typical room data containing fields such as ID, name, capacity, and sensor IDs. The performance and usability benefits of the single-request approach far outweigh the minimal increase in payload size, making it the clearly superior design choice for this use case.

2.2 Room Deletion & Safety Logic - DELETE Idempotency

Yes, the DELETE operation is idempotent in this implementation. Idempotency is a property where performing the same operation multiple times produces the same server state as performing it once.
When a client sends a DELETE request for a room that currently exists in the system, the room is removed from the DataStore and the server returns a 204 No Content response, indicating successful deletion. If the same client mistakenly sends the exact same DELETE request a second or third time, the room no longer exists in the system. Rather than causing an error or unintended side effect, the server simply returns a 404 Not Found response. Although the HTTP status code returned differs between the first and subsequent requests, the actual state of the server remains identical after all of them, the room is absent from the system in every case.
This behaviour is fully consistent with the REST specification, which defines DELETE as an idempotent method. The key distinction is between the server state and the response code, idempotency refers to the server state remaining unchanged, not to the response being identical. No data is modified, created, or corrupted by repeated DELETE calls, which means clients can safely retry a DELETE request in cases of network failure without risking unintended consequences.


**Part 3: Sensor Operations & Linking**

3.1 Sensor Resource & Integrity - @Consumes Annotation

The @Consumes(MediaType.APPLICATION_JSON) annotation is placed on the POST method to declare that this endpoint exclusively accepts request bodies with a Content-Type header of application/json. This creates a strict and enforceable contract between the server and its clients regarding the format of data that will be accepted.
If a client attempts to send data in an unsupported format such as text/plain or application/xml, JAX-RS intercepts the request before it even reaches the method body and automatically returns an HTTP 415 Unsupported Media Type response. This rejection happens at the framework level, meaning no application code is executed and no partial processing occurs. The client is clearly informed that the format they used is not acceptable.
This behaviour is highly beneficial for several reasons. It prevents the server from attempting to deserialise data it cannot process, which would otherwise result in confusing runtime errors. It enforces a clear and consistent API contract, ensuring that all data entering the system is in the expected JSON format. It also protects the application from receiving malformed or unexpected input that could cause unpredictable behaviour downstream in the processing pipeline.

3.2 Filtered Retrieval & Search - Query Parameter vs Path Parameter

Using a query parameter for filtering, as implemented with GET /api/v1/sensors?type=CO2, is the correct and widely accepted approach in RESTful API design, and it is clearly superior to embedding the filter value in the URL path.
The primary reason is that query parameters are inherently optional. When the type parameter is not included, the endpoint simply returns all sensors. When it is included, the results are filtered accordingly. This makes the same endpoint reusable for both the full list and filtered subsets without requiring separate URLs. In contrast, using a path-based design such as /api/v1/sensors/type/CO2 implies that type/CO2 is itself a permanent, addressable resource that exists on the server, which is semantically incorrect and misleading to client developers.
Path parameters are designed to identify a specific unique resource, such as /api/v1/sensors/TEMP-001 which pinpoints one specific sensor. Query parameters are designed precisely for the purpose of filtering, searching, and sorting collections. Additionally, query parameters are far easier to extend. For example, adding a second filter condition such as ?type=CO2&status=ACTIVE requires no changes to the URL structure whatsoever, whereas a path-based approach would require defining entirely new route patterns. This flexibility makes query parameters the correct tool for collection filtering.


**Part 4: Deep Nesting with Sub-Resources**

4.1 The Sub-Resource Locator Pattern

The Sub-Resource Locator pattern is an architectural approach in JAX-RS where a parent resource class does not directly handle requests for a nested path. Instead, it contains a locator method that instantiates and returns a separate dedicated resource class, which JAX-RS then uses to handle the actual request.
In this API, the SensorResource class contains a method annotated with @Path("{sensorId}/readings") that does not itself process the request. Instead, it creates and returns a new instance of SensorReadingResource, passing the sensorId as context. JAX-RS recognises this as a sub-resource locator and delegates all further request handling, including GET to retrieve reading history and POST to add new readings to the SensorReadingResource class.
This pattern delivers substantial architectural benefits, particularly for large and complex APIs. The most important benefit is the separation of concerns, each class has a single, well-defined responsibility. SensorResource manages sensor-level operations and SensorReadingResource manages reading-level operations. If all nested paths were handled within a single massive controller class, that class would grow unmanageably large as the API expands, making it extremely difficult to read, debug, and modify without introducing regressions.
The pattern also significantly improves maintainability. Changes to reading logic are confined entirely to SensorReadingResource and have no impact on SensorResource. Each class can also be tested independently and in isolation, which is a critical practice in professional software engineering. This approach mirrors how real-world enterprise APIs are structured, where large systems are decomposed into focused, modular components that can be developed and maintained by separate teams.


**Part 5: Advanced Error Handling, Exception Mapping & Logging**

5.1 Dependency Validation - 422 vs 404

The choice between HTTP 422 Unprocessable Entity and HTTP 404 Not Found is an important semantic distinction that directly affects how clearly the API communicates errors to its clients.
A 404 Not Found response specifically means that the requested URL or endpoint does not exist on the server. In this scenario, the client sends a POST request to /api/v1/sensors, which is a perfectly valid and existing endpoint. The URL itself is entirely correct, so returning a 404 would be factually wrong and would mislead the client into thinking the endpoint does not exist, causing unnecessary confusion and wasted debugging effort.
The actual problem is that the JSON payload contains a roomId field whose value references a room that does not exist in the system. The request structure is syntactically valid and well-formed JSON, the server can parse it without issue. However, the semantic content of the data is invalid because it contains a broken reference. HTTP 422 Unprocessable Entity is precisely designed for this situation. It communicates that the server fully understands the request and the endpoint is correct, but it cannot complete the operation because the data contains a logical or semantic error. This gives the client a much more accurate and actionable error message, allowing them to immediately identify that the problem is with the data they submitted rather than with the URL they used.

5.2 The Global Safety Net - Stack Trace Security Risks

Exposing raw Java stack traces to external API consumers is a significant cybersecurity risk. Stack traces contain highly detailed technical information about the internal structure and state of the application at the point of failure, which attackers can systematically exploit.
A stack trace can expose the full class and package names of the application, revealing the entire internal architecture and codebase organisation to anyone who reads it. It discloses the names and exact version numbers of third-party libraries and frameworks such as Jersey, Jackson, or Grizzly, enabling an attacker to cross-reference those versions against publicly known CVE databases to find unpatched vulnerabilities that can be directly exploited. File names and precise line numbers indicate exactly where in the source code a failure occurred, giving an attacker a surgical target for crafting repeated malformed requests designed to trigger specific failure points and probe the system's behaviour. Server configuration details and Java runtime version information may also be revealed.
As a concrete example, if a stack trace reveals that the application is using Jersey version 3.0.1 and a known remote code execution vulnerability exists in that version, an attacker now has everything they need to launch a targeted attack. By implementing a catch-all ExceptionMapper for Throwable that intercepts every unhandled exception and returns only a generic HTTP 500 Internal Server Error message with no technical details, the API ensures that none of this sensitive internal information is ever exposed to the outside world, eliminating this entire class of information disclosure vulnerability.

5.3 API Request & Response Logging Filters - Filters vs Manual Logging

Using JAX-RS ContainerRequestFilter and ContainerResponseFilter for logging is architecturally far superior to manually inserting Logger.info() statements inside every individual resource method, for several interconnected reasons.
The most fundamental benefit is the complete elimination of code duplication. Without filters, identical logging code would need to be written inside every single method across every resource class in the application. In a system with multiple resource classes each containing several endpoints, this amounts to dozens of repetitive logging statements scattered throughout the codebase. This is not only time-consuming to implement but creates a serious maintenance burden, if the log format, log level, or logged fields ever need to change, every single instance must be located and updated individually, with a high risk of missing some.
Filters enforce the principle of separation of concerns, which is a foundational principle of clean software design. Logging is a cross-cutting concern, it applies uniformly to all requests and responses regardless of their content or purpose. It has no relationship to the business logic that individual resource methods implement and therefore should not be mixed in with that logic. Keeping them separate makes both the logging code and the business logic easier to read, understand, and modify independently.
Critically, filters provide guaranteed and automatic coverage of every request and response that passes through the application. A newly added endpoint is automatically logged without any additional action from the developer. With manual logging, there is always a risk of forgetting to add a logging statement to a new method, resulting in gaps in observability. Filters make comprehensive logging the default behaviour rather than a task that must be remembered, which is the correct approach for building reliable and observable production APIs.

