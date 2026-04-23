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

## Simple Examples (curl)

- **See the API links:** `curl http://localhost:8081/api/`
- **Get all rooms:** `curl http://localhost:8081/api/rooms`
- **Add a new room:** `curl -X POST http://localhost:8081/api/rooms -H "Content-Type: application/json" -d '{"id":"ROOM-101","name":"Classroom 101","capacity":40}'`
- **Search for sensors:** `curl http://localhost:8081/api/sensors?type=Temperature`
- **Add a sensor reading:** `curl -X POST http://localhost:8081/api/sensors/TEMP-001/readings -H "Content-Type: application/json" -d '{"value": 22.5}'`

---

## Author
- **Name:** Osandi Randeniya  
- **UOW ID:** w2153603  
- **IIT ID:** 20242020  
- **Module:** 5COSC022C.2 Client-Server Architectures  

---

## Answers to Questions

**1. How it works (Architecture)**
In Jersey, a new object is usually created for every single request. This means we can't just save data inside the class variables. So, I used a `DataStore` class (a Singleton) to keep all our data in one place. I also used `ConcurrentHashMap` to make sure the data stays safe even if many people use the API at the same time.

**2. Discovery Endpoint**
I added links in the response so anyone using the API can easily find their way around. It makes the API much easier to use!

**3. Room Management**
When you ask for a list of rooms, I send back all the details (like name and capacity) instead of just IDs. This saves the user from having to make many extra requests to get the info they need.

**4. Deleting Rooms**
I made the DELETE operation "idempotent," which means if you try to delete the same thing twice, nothing bad happens. I also added a safety check so you can't delete a room if it still has sensors inside it.

**5. JSON and Errors**
I used `@Consumes` to make sure we only accept JSON data. I also made special "Exception Mappers" so that if something goes wrong, the API sends back a nice, clear error message in JSON format.

**6. Logging**
I added a special filter that logs every single request and response. This is way better than manually writing print statements in every function!
