// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

package com.smartcampus.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// This class catches any unexpected errors and sends a nice message to the user
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable e) {
        // Log the error so we can fix it later
        LOGGER.log(Level.SEVERE, "Unhandled exception: ", e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                    "error", "INTERNAL_SERVER_ERROR",
                    "message", "An unexpected error occurred. Please contact support."
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
