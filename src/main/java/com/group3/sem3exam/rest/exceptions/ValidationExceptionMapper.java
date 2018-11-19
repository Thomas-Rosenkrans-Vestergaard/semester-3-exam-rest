package com.group3.sem3exam.rest.exceptions;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.context.FieldContext;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException>
{

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private ServletContext context;

    @Override public Response toResponse(ValidationException exception)
    {
        boolean isDebug = "true".equals(context.getInitParameter("debug"));

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.exception = exception.getClass().getSimpleName();
        exceptionResponse.message = exception.getMessage();
        exceptionResponse.responseCode = exception.getResponseCode();
        exceptionResponse.debug = isDebug;
        for (ConstraintViolation constraintViolation : exception.constraintViolations) {
            if (constraintViolation.getCauses() != null) {
                for (ConstraintViolation cause : constraintViolation.getCauses())
                    exceptionResponse.violations.add(new ConstraintViolationDTO(constraintViolation, cause));
            } else
                exceptionResponse.violations.add(new ConstraintViolationDTO(constraintViolation));
        }

        return Response.status(exception.getResponseCode()).entity(gson.toJson(exceptionResponse)).build();
    }

    private class ExceptionResponse
    {
        public String exception;
        public String message;

        public Integer                      responseCode;
        public Boolean                      debug;
        public List<ConstraintViolationDTO> violations = new ArrayList<>();
    }

    private class ConstraintViolationDTO
    {
        public String     message;
        public String     checkName;
        public String     attribute;
        public String     invalidValue;
        public JsonObject variables;

        public ConstraintViolationDTO(ConstraintViolation parent, ConstraintViolation cause)
        {

            String message = cause.getMessage();
            this.message = getAttributeName(parent) + "." + getAttributeName(cause)  + message.substring(message.indexOf(" "));
            this.checkName = cause.getCheckName().substring(cause.getCheckName().lastIndexOf(".") + 1);
            this.variables = getVariables(cause);
            if (cause.getContext() instanceof FieldContext) {
                this.attribute = String.format("%s.%s", getAttributeName(parent), getAttributeName(cause));
                this.invalidValue = String.valueOf(cause.getInvalidValue());
            }
        }

        public ConstraintViolationDTO(ConstraintViolation constraintViolation)
        {
            String message = constraintViolation.getMessage();

            this.message = getAttributeName(constraintViolation) + message.substring(message.indexOf(" "));
            this.checkName = constraintViolation.getCheckName().substring(constraintViolation.getCheckName().lastIndexOf(".") + 1);
            this.variables = getVariables(constraintViolation);
            this.invalidValue = String.valueOf(constraintViolation.getInvalidValue());
            if (constraintViolation.getContext() instanceof FieldContext)
                attribute = getAttributeName(constraintViolation);
        }
    }

    private String getAttributeName(ConstraintViolation constraintViolation)
    {
        FieldContext fieldContext = (FieldContext) constraintViolation.getContext();

        return fieldContext.getField().getName();
    }

    private JsonObject getVariables(ConstraintViolation constraintViolation)
    {

        JsonObject result = new JsonObject();
        Map<String, ? extends Serializable> variablesMap = constraintViolation.getMessageVariables();
        if (variablesMap != null && !variablesMap.isEmpty()) {
            for (Map.Entry<String, ? extends Serializable> entry : variablesMap.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();

                if (val instanceof String)
                    result.addProperty(key, (String) val);
                else if (val instanceof Number)
                    result.addProperty(key, (Number) val);
                else if (val instanceof Boolean)
                    result.addProperty(key, (Boolean) val);
            }
        }

        return result;
    }
}
