package com.group3.sem3exam.rest.exceptions;

import java.util.List;

public class ExceptionResponse
{
    public String       exception;
    public String       message;
    public Integer      responseCode;
    public Boolean      debug;
    public Throwable    cause;
    public List<String> validationErrors;
}
