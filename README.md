# Name: Osandi Randeniya, UOW ID: w2153603, IIT ID: 20242020

# Smart Campus — Sensor & Room Management API

## Overview

Hi! I am Osandi. This is my project for the Client-Server Architectures coursework. I built a simple API to help manage rooms and sensors in a university campus.

I used Java with Jersey and Grizzly to make this. All the data is stored in memory, which makes it super fast!

---

## Project Demo Video
You can watch my project demo video here: [Watch on YouTube](https://youtu.be/Z0F1JVWYKrQ?si=7xN_8QwawOTYYzzj)

---

## How to Build and Run

### What you need
- Java 11 or newer
- NetBeans IDE

### How to do it

1. **Clone the code**
   `git clone https://github.com/osandipabasara-design/SmartCampusAPI.git`

2. **Open it in NetBeans**
   Just open the folder as a project in NetBeans.

3. **Build the project**
   Right-click and choose "Clean and Build". Maven will handle all the libraries for us.

4. **Run the server**
   Run the `Main.java` file. You will see a message saying "SERVER IS READY!".

5. **Test it**
   Go to `http://localhost:8081/api/` in your browser or Postman.

---

## Sample curl Commands

### 1. Discovery endpoint
`curl http://localhost:8081/api/`

### 2. Get all rooms
`curl http://localhost:8081/api/rooms`

### 3. Create a new room
`curl -X POST http://localhost:8081/api/rooms -H "Content-Type: application/json" -d '{"id":"HALL-01","name":"Main Hall","capacity":200}'`

### 4. Filter sensors by type
`curl http://localhost:8081/api/sensors?type=CO2`

### 5. Add a sensor reading
`curl -X POST http://localhost:8081/api/sensors/TEMP-001/readings -H "Content-Type: application/json" -d '{"value": 23.7}'`

### 6. Delete a room that has sensors (returns 409 Conflict)
`curl -X DELETE http://localhost:8081/api/rooms/LIB-301`

### 7. Create sensor with invalid roomId (returns 422 error)
`curl -X POST http://localhost:8081/api/sensors -H "Content-Type: application/json" -d '{"id":"TEMP-999","type":"Temperature","status":"ACTIVE", "currentValue":0,"roomId":"FAKE-ROOM"}'`

### 8. Post reading to maintenance sensor (returns 403 Forbidden)
`curl -X POST http://localhost:8081/api/sensors/CO2-002/readings -H "Content-Type: application/json" -d '{"value": 450.0}'`

---

## Author
- **Name:** Osandi Randeniya  
- **UOW ID:** w2153603  
- **IIT ID:** 20242020  
- **Module:** 5COSC022C.2 Client-Server Architectures  

---

## Answers to Coursework Questions

**1. How it works (Architecture)**
In Jersey, a new object is usually created for every single request. This means we can't just save data inside the class variables. So, I used a `DataStore` class (a Singleton) to keep all our data in one place. I also used `ConcurrentHashMap` to make sure the data stays safe even if many people use the API at the same time. I also used **Jackson** so that the API can understand and send JSON data automatically.

**2. Discovery Endpoint (HATEOAS)**
I added a "Discovery" endpoint which is like a home page for the API. It contains links to other parts of the system, like rooms and sensors. This makes the API "self-documenting" because the user doesn't have to guess the URLs!

**3. Room Management (Resource Design)**
When you ask for a list of rooms, I send back all the details (like name and capacity) instead of just the IDs. This is much better because it saves the user from having to make dozens of extra requests just to see the names of the rooms.

**4. Deleting Rooms (Idempotency)**
I made the DELETE operation "idempotent," which means deleting something once or twice has the same effect on the server. I also added a safety check: if a room still has sensors in it, the API will block the deletion and send an error message.

**5. Sub-Resources (Nesting)**
For the sensor readings, I used a "Sub-resource" pattern. This means the readings are nested inside the sensors (like `/sensors/ID/readings`). This makes the API structure very logical and easy to follow.

**6. JSON and Error Handling**
I used `@Consumes` and `@Produces` to make sure the API only speaks JSON. I also built special **Exception Mappers**. For example, if you try to add a sensor to a room that doesn't exist, the API sends back a clear `422 Unprocessable Entity` error instead of just crashing.

**7. Logging and Filters**
Instead of writing log messages in every single function, I used a **Logging Filter**. This automatically records every request and response that comes through the API, making it much easier to debug and keep track of what's happening.
