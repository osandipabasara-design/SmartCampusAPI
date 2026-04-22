# Name: Osandi Randeniya, UOW ID: w2153603, IIT ID: 20242020

# Smart Campus — Sensor & Room Management API

## Overview

Hi! I am Osandi. This is my project for the Client-Server Architectures coursework. It is a simple API to manage rooms and sensors in a university campus.

I used Java with Jersey and Grizzly to build this. All the data is stored in memory, so it is very fast but it resets when you restart the server.

---

## How to Build and Run

### What you need
- Java 11 or newer
- NetBeans IDE

### How to do it

1. **Clone the code**
   `git clone https://github.com/YourUsername/SmartCampusAPI.git`

2. **Open it in NetBeans**
   Just open the folder as a project in NetBeans.

3. **Build the project**
   Right-click and choose "Clean and Build". Maven will handle everything.

4. **Run the server**
   Run the `Main.java` file. You will see a message saying the API started!

5. **Test it**
   Go to `http://localhost:8080/api/v1` in your browser.

---

## Simple Examples (curl)

- **See the API links:** `curl http://localhost:8080/api/v1`
- **Get all rooms:** `curl http://localhost:8080/api/v1/rooms`
- **Add a new room:** `curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d '{"id":"HALL-01","name":"Main Hall","capacity":200}'`
- **Search for CO2 sensors:** `curl http://localhost:8080/api/v1/sensors?type=CO2`
- **Add a sensor reading:** `curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings -H "Content-Type: application/json" -d '{"value": 23.7}'`

---

## Author
- **Name:** Osandi Randeniya  
- **UOW ID:** w2153603  
- **IIT ID:** 20242020  
- **Module:** 5COSC022C.2 Client-Server Architectures  

---

## Answers to Questions

**1. How it works (Architecture)**
JAX-RS usually creates a new object for every request. Because of this, we can't save data inside the classes. I used a `DataStore` class which is a "Singleton" (meaning only one exists) to keep all the data. I also used `ConcurrentHashMap` so that if many people use the API at once, the data stays safe.

**2. Discovery Endpoint**
I added links in the response so that someone using the API knows where to go next. This makes the API easy to use without looking at the manual all the time.

**3. Room Management**
When you ask for a list of rooms, I send all the room details instead of just IDs. This is better because the user doesn't have to make 100 more requests just to see the room names.

**4. Deleting Rooms**
If you try to delete a room that doesn't exist, it returns a 404. If it is already deleted, it still works fine (idempotent). But I made sure you can't delete a room if it still has sensors in it!

**5. JSON and Errors**
I used `@Consumes` to make sure we only accept JSON data. If someone sends something else, the server says it's not supported. I also used special "Exception Mappers" to catch errors and show them in a nice way to the user.

**6. Logging**
I added a filter that logs every request and response. This is much better than putting print statements everywhere in the code!
