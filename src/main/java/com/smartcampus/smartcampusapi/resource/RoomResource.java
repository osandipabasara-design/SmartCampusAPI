/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi.resource;

import com.smartcampus.smartcampusapi.exception.RoomNotEmptyException;
import com.smartcampus.smartcampusapi.model.Room;
import com.smartcampus.smartcampusapi.storage.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author osandirandeniya
 */

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    
    @GET
    public Response getAllRooms() {
        return Response.ok(DataStore.rooms.values()).build();
    }

    @POST
    public Response createRoom(Room room) {
        if (DataStore.rooms.containsKey(room.getId())) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Room ID already exists.");
            return Response.status(409).entity(err).build();
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Room not found: " + roomId);
            return Response.status(404).entity(err).build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Room not found: " + roomId);
            return Response.status(404).entity(err).build();
        }
        // Business rule: cannot delete a room that still has sensors
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException();
        }
        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
    
}
