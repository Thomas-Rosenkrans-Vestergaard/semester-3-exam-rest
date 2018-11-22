package com.group3.sem3exam.rest.exceptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.logic.FacadeException;
import com.group3.sem3exam.logic.ResourceConflictException;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.validation.ResourceValidationException;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class FacadeExceptionMapper implements ExceptionMapper<FacadeException>
{

    private static Gson                gson  = new GsonBuilder().setPrettyPrinting().create();
    private static Map<Class, Integer> codes = new HashMap<>();

    static {
        codes.put(ResourceValidationException.class, 422);
        codes.put(ResourceNotFoundException.class, 404);
        codes.put(AuthenticationException.class, 401);
        codes.put(ResourceConflictException.class, 409);
    }

    @Context
    private ServletContext context;

    @Override
    public Response toResponse(FacadeException exception)
    {
        boolean isDebug = "true".equals(context.getInitParameter("debug"));

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.errorName = exception.getErrorName();
        exceptionResponse.message = exception.getErrorMessage();
        exceptionResponse.responseCode = codes.getOrDefault(exception.getClass(), 500);
        exceptionResponse.debug = isDebug;
        if (isDebug)
            exceptionResponse.cause = exception.getCause();

        return Response.status(exceptionResponse.responseCode).entity(gson.toJson(exceptionResponse)).build();
    }
}
