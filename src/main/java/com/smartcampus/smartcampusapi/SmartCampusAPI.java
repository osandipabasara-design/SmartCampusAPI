/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.smartcampus.smartcampusapi;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

/**
 *
 * @author osandirandeniya
 */
public class SmartCampusAPI {
    
    public static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) throws Exception {
        ResourceConfig config = new ResourceConfig()
                .packages("com.smartcampus");

        HttpServer server = GrizzlyHttpServerFactory
                .createHttpServer(URI.create(BASE_URI), config);

        System.out.println("==============================================");
        System.out.println(" Smart Campus API is running!");
        System.out.println(" Visit: http://localhost:8080/api/v1");
        System.out.println(" Press ENTER to stop the server.");
        System.out.println("==============================================");

        System.in.read();
        server.stop();
    }
}
