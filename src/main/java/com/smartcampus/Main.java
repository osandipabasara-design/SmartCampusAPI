// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {

    // The base URL for our server
    public static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) throws Exception {
        // Setting up Jersey and telling it where to find our code
        ResourceConfig config = new ResourceConfig()
                .packages(
                    "com.smartcampus.resource",
                    "com.smartcampus.exception",
                    "com.smartcampus.filter"
                )
                .register(JacksonFeature.class); // This helps with JSON

        // Starting the Grizzly server
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI + "api/v1"), config);

        System.out.println("===================================================");
        System.out.println("Smart Campus API started!");
        System.out.println("Discovery endpoint: " + BASE_URI + "api/v1");
        System.out.println("Press ENTER to stop the server.");
        System.out.println("===================================================");

        // Keep the server running until Enter is pressed
        System.in.read(); 
        server.shutdown();
    }
}
