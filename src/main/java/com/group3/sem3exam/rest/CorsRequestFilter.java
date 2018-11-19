package com.group3.sem3exam.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class CorsRequestFilter implements ContainerRequestFilter
{

    @Override public void filter(ContainerRequestContext requestContext) throws IOException
    {
        if (requestContext.getRequest().getMethod().equals("OPTIONS")) {
            requestContext.abortWith(Response.status(Response.Status.OK).build());
        }
    }
}
