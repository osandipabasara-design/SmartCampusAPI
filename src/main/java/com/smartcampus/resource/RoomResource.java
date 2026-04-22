// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final DataStore store = DataStore.getInstance();

    // Get a list of all rooms
    @GET
    public Response getAllRooms() {
        List<Room> roomList = store.getRooms().values().stream()
                .collect(Collectors.toList());
        return Response.ok(roomList).build();
    }

    // Create a new room
    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Room ID is required."))
                    .build();
        }
        if (store.getRooms().containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Room already exists: " + room.getId()))
                    .build();
        }
        store.getRooms().put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // Get a single room by its ID
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found: " + roomId))
                    .build();
        }
        return Response.ok(room).build();
    }

    // Delete a room if it is empty
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found: " + roomId))
                    .build();
        }

        // We can't delete a room if it still has sensors
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }

        store.getRooms().remove(roomId);
        return Response.noContent().build(); 
    }
}
