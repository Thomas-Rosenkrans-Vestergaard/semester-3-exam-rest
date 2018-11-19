package com.group3.sem3exam.rest.exceptions;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.group3.sem3exam.logic.validation.ResourceValidationException;
import com.group3.sem3exam.logic.validation.ValidationViolation;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class ResourceValidationExceptionMapper implements ExceptionMapper<ResourceValidationException>
{

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private ServletContext context;

    @Override
    public Response toResponse(ResourceValidationException exception)
    {
        boolean isDebug = "true".equals(context.getInitParameter("debug"));

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.errorName = exception.getErrorName();
        exceptionResponse.message = exception.getErrorMessage();
        exceptionResponse.responseCode = 422;
        exceptionResponse.debug = isDebug;
        exceptionResponse.violations = exception.getValidationViolations()
                                                .stream()
                                                .map(v -> new ValidationViolationDTO(v))
                                                .collect(Collectors.toList());

        return Response.status(422).entity(gson.toJson(exceptionResponse)).build();
    }

    private class ExceptionResponse
    {
        public String                       errorName;
        public String                       message;
        public Integer                      responseCode;
        public Boolean                      debug;
        public List<ValidationViolationDTO> violations = new ArrayList<>();
    }

    private class ValidationViolationDTO
    {
        public String     message;
        public String     checkName;
        public String     attribute;
        public Object     invalidValue;
        public JsonObject variables;

        public ValidationViolationDTO(ValidationViolation violation)
        {

            String message = violation.getMessage();
            this.message = violation.getAttribute() + message.substring(message.indexOf(" "));
            this.checkName = violation.getCheckName();
            this.variables = getVariables(violation.getVariables());
            this.attribute = violation.getAttribute();
            this.invalidValue = violation.getInvalidValue();
        }
    }

    private JsonObject getVariables(Map<String, ? extends Serializable> variables)
    {
        JsonObject result = new JsonObject();
        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, ? extends Serializable> entry : variables.entrySet()) {
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
