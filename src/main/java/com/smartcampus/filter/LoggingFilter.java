// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

package com.smartcampus.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

// This class logs every request and response that comes through the API
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    // This runs when a request comes in
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.info(">>> REQUEST: " 
            + requestContext.getMethod() + " " 
            + requestContext.getUriInfo().getRequestUri());
    }

    // This runs when a response is sent out
    @Override
    public void filter(ContainerRequestContext requestContext, 
                       ContainerResponseContext responseContext) throws IOException {
        LOGGER.info("<<< RESPONSE: Status " + responseContext.getStatus());
    }
}
