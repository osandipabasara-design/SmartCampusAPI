// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

package com.smartcampus.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

// This handles the "Room not empty" error and sends back a JSON response
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", "ROOM_NOT_EMPTY", "message", e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
