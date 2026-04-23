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

    // Using 0.0.0.0 to make sure it listens on all addresses
    public static final String BASE_URI = "http://0.0.0.0:8081/api/";

    public static void main(String[] args) throws Exception {
        System.out.println("Starting server at: http://localhost:8081/api/");

        // Manually registering classes just in case package scanning is failing
        ResourceConfig config = new ResourceConfig()
                .packages("com.smartcampus.resource", "com.smartcampus.exception", "com.smartcampus.filter")
                .register(JacksonFeature.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        System.out.println("===================================================");
        System.out.println("SERVER IS READY!");
        System.out.println("Try this in Postman: http://localhost:8081/api/rooms");
        System.out.println("===================================================");

        System.in.read(); 
        server.shutdown();
    }
}
