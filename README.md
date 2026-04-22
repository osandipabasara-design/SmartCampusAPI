# Smart Campus API

A RESTful Web Service built with **Java (JAX-RS)** using **Jersey** and the **Grizzly HTTP Server**. This API allows for the management of rooms, sensors, and sensor readings within a campus environment.

## 🚀 Features
- **Room Management**: Create, retrieve, and delete rooms.
- **Sensor Integration**: Link sensors to specific rooms.
- **Data Monitoring**: Track real-time readings from sensors.
- **Error Handling**: Custom exception mapping for not found resources and business rule violations.
- **In-Memory Storage**: Uses a concurrent data store (no external database required for testing).

## 🛠 Technology Stack
- **Language**: Java 17
- **Framework**: Jersey (JAX-RS)
- **Server**: Grizzly HTTP Server
- **JSON Provider**: Jackson
- **Build Tool**: Maven

## 🏁 Getting Started

### 1. Prerequisites
- Java JDK 17 or higher
- Apache Maven

### 2. Run the Application
Navigate to the project root and run the following command:
```bash
mvn clean compile exec:java
```
The server will start at: `http://localhost:8080/api/v1/`

---

## 📡 API Endpoints & Postman Workflow

Follow this order to test the API correctly (since the data is in-memory and starts empty).

### 1. Rooms
| Method | URL | Description | JSON Body Example |
| :--- | :--- | :--- | :--- |
| **POST** | `/rooms` | Create a room | `{"id": "R1", "name": "Lab 1", "capacity": 30}` |
| **GET** | `/rooms` | List all rooms | N/A |
| **GET** | `/rooms/{id}` | Get room details | N/A |
| **DELETE** | `/rooms/{id}` | Delete a room | N/A |

### 2. Sensors
| Method | URL | Description | JSON Body Example |
| :--- | :--- | :--- | :--- |
| **POST** | `/sensors` | Add a sensor | `{"id": "S1", "type": "Temp", "roomId": "R1", "status": "ACTIVE"}` |
| **GET** | `/sensors` | List all sensors | N/A |
| **GET** | `/sensors/{id}` | Get sensor details | N/A |

### 3. Sensor Readings
| Method | URL | Description | JSON Body Example |
| :--- | :--- | :--- | :--- |
| **POST** | `/sensors/{id}/readings` | Add a reading | `{"value": 22.5}` |
| **GET** | `/sensors/{id}/readings` | List readings | N/A |

---

## 📂 Project Structure
- `src/main/java/.../resource`: JAX-RS Resource classes (API Endpoints).
- `src/main/java/.../model`: Plain Old Java Objects (POJOs) for data.
- `src/main/java/.../storage`: Static `DataStore` for in-memory persistence.
- `src/main/java/.../exception`: Exception mappers for custom HTTP error responses.

## ⚠️ Important Note
This project uses **In-Memory Storage**. All data created via Postman will be **erased** when you stop or restart the Java server.
