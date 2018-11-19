package com.group3.sem3exam.rest.exceptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception>
{

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private ServletContext context;

    @Override public Response toResponse(Exception exception)
    {
        boolean isDebug = "true".equals(context.getInitParameter("debug"));

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.exception = exception.getClass().getSimpleName();
        exceptionResponse.message = "An unexpected error occurred.";
        exceptionResponse.debug = isDebug;
        exceptionResponse.responseCode = 500;
        if (isDebug) {
            exceptionResponse.cause = exception;
        }

        return Response.status(500).entity(gson.toJson(exceptionResponse)).build();
    }
}
