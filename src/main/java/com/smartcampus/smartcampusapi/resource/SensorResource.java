/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi.resource;

import com.smartcampus.smartcampusapi.exception.LinkedResourceNotFoundException;
import com.smartcampus.smartcampusapi.model.Room;
import com.smartcampus.smartcampusapi.model.Sensor;
import com.smartcampus.smartcampusapi.storage.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author osandirandeniya
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> result = new ArrayList<>(DataStore.sensors.values());

        // Filter by type if query param is provided
        if (type != null && !type.isEmpty()) {
            result.removeIf(s -> !s.getType().equalsIgnoreCase(type));
        }
        return Response.ok(result).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        // Validate that the roomId actually exists
        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Room not found with ID: " + sensor.getRoomId());
        }

        DataStore.sensors.put(sensor.getId(), sensor);

        // Link sensor to the room
        Room room = DataStore.rooms.get(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());

        // Initialise an empty readings list for this sensor
        DataStore.readings.put(sensor.getId(), new ArrayList<>());

        return Response.status(201).entity(sensor).build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Sensor not found: " + sensorId);
            return Response.status(404).entity(err).build();
        }
        return Response.ok(sensor).build();
    }

    // Sub-resource locator — delegates to SensorReadingResource
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsResource(
            @PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}