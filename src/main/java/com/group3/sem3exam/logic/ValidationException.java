package com.group3.sem3exam.logic;

import com.group3.sem3exam.logic.FacadeException;
import net.sf.oval.ConstraintViolation;

import java.util.List;

public class ValidationException extends FacadeException
{

    public final List<ConstraintViolation> constraintViolations;

    public ValidationException(String message, List<ConstraintViolation> constraintViolations)
    {
        super(message, "Could not validate resource.", 422, null);
        this.constraintViolations = constraintViolations;
    }
}
