// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

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

    // Get all the readings recorded for this sensor
    @GET
    public Response getAllReadings() {
        // If the sensor doesn't exist, return an error
        if (!store.getSensors().containsKey(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(java.util.Map.of("error", "Sensor not found: " + sensorId))
                    .build();
        }
        List<SensorReading> list = store.getReadings()
                .getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(list).build();
    }

    // Add a new measurement reading for this sensor
    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(java.util.Map.of("error", "Sensor not found: " + sensorId))
                    .build();
        }

        // Don't allow readings if the sensor is being fixed
        if ("MAINTENANCE".equals(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId);
        }

        // Create the reading and add it to our list
        SensorReading newReading = new SensorReading(reading.getValue());
        store.getReadings()
             .computeIfAbsent(sensorId, k -> new ArrayList<>())
             .add(newReading);

        // Update the sensor's current value too
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(newReading).build();
    }
}
