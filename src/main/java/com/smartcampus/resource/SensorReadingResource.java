/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;
    private final DataStore store = DataStore.getInstance();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET /api/v1/sensors/{sensorId}/readings
    @GET
    public Response getAllReadings() {
        // Make sure the sensor exists
        if (!store.getSensors().containsKey(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(java.util.Map.of("error", "Sensor not found: " + sensorId))
                    .build();
        }
        List<SensorReading> list = store.getReadings()
                .getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(list).build();
    }

    // POST /api/v1/sensors/{sensorId}/readings
    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(java.util.Map.of("error", "Sensor not found: " + sensorId))
                    .build();
        }

        // Part 5.3: block readings if sensor is under MAINTENANCE
        if ("MAINTENANCE".equals(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId);
        }

        // Create and save the reading
        SensorReading newReading = new SensorReading(reading.getValue());
        store.getReadings()
             .computeIfAbsent(sensorId, k -> new ArrayList<>())
             .add(newReading);

        // Part 4.2: update the parent sensor's currentValue
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(newReading).build();
    }
}
