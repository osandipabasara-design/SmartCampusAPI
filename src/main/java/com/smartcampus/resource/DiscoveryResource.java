// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    // This shows all the available links in our API
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("api", "Smart Campus Sensor & Room Management API");
        info.put("version", "v1");
        info.put("contact", "admin@smartcampus.ac.uk");
        info.put("resources", Map.of(
            "rooms",   "/api/v1/rooms",
            "sensors", "/api/v1/sensors"
        ));
        return Response.ok(info).build();
    }
}
