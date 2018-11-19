package com.group3.sem3exam.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class
CorsResponseFilter implements ContainerResponseFilter
{
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException
    {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*" );
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true" );
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, Accept, Content-Type, Authorization");
    }
}
