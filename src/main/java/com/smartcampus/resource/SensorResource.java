// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore store = DataStore.getInstance();

    // Get all sensors, can filter by type too
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> result = store.getSensors().values().stream()
                .filter(s -> type == null || s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        return Response.ok(result).build();
    }

    // Add a new sensor to the system
    @POST
    public Response createSensor(Sensor sensor) {
        // Check if the room exists first
        if (sensor.getRoomId() == null || !store.getRooms().containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Room with ID '" + sensor.getRoomId() + "' does not exist. Cannot register sensor.");
        }

        // Give it an ID if it doesn't have one
        if (sensor.getId() == null || sensor.getId().isBlank()) {
            sensor.setId("SENSOR-" + System.currentTimeMillis());
        }

        // Make sure the ID isn't already used
        if (store.getSensors().containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Sensor ID already exists: " + sensor.getId()))
                    .build();
        }

        store.getSensors().put(sensor.getId(), sensor);

        // Link the sensor to the room it belongs to
        Room room = store.getRooms().get(sensor.getRoomId());
        if (!room.getSensorIds().contains(sensor.getId())) {
            room.getSensorIds().add(sensor.getId());
        }

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    // Get details of one specific sensor
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = store.getSensors().get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found: " + sensorId))
                    .build();
        }
        return Response.ok(sensor).build();
    }

    // Path for sensor readings
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
