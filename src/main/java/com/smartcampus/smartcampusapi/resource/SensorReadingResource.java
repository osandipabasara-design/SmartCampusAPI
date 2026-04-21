/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.smartcampus.smartcampusapi.resource;

import com.smartcampus.smartcampusapi.exception.SensorUnavailableException;
import com.smartcampus.smartcampusapi.model.Sensor;
import com.smartcampus.smartcampusapi.model.SensorReading;
import com.smartcampus.smartcampusapi.storage.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Sensor not found: " + sensorId);
            return Response.status(404).entity(err).build();
        }
        List<SensorReading> list =
            DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(list).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Sensor not found: " + sensorId);
            return Response.status(404).entity(err).build();
        }

        // Block readings for sensors under maintenance
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId);
        }

        // Auto-generate ID and timestamp if not provided
        if (reading.getId() == null) {
            reading = new SensorReading(reading.getValue());
        }

        // Store the reading
        DataStore.readings
            .computeIfAbsent(sensorId, k -> new ArrayList<>())
            .add(reading);

        // Update currentValue on the parent sensor (side effect)
        sensor.setCurrentValue(reading.getValue());

        return Response.status(201).entity(reading).build();
    }
}