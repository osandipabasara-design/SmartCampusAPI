/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author osandirandeniya
 */
@Provider
public class LinkedResourceNotFoundMapper
        implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException e) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "UNPROCESSABLE_ENTITY");
        body.put("message", e.getMessage());
        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(body).build();
    }
}