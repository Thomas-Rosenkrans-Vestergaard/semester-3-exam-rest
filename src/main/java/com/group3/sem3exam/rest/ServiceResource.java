package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.*;
import com.group3.sem3exam.logic.AuthenticationFacade;
import com.group3.sem3exam.logic.ResourceConflictException;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.SpecializedGson;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.jwt.JwtGenerationException;
import com.group3.sem3exam.logic.authorization.AuthorizationException;
import com.group3.sem3exam.logic.services.PermissionTemplateTransfer;
import com.group3.sem3exam.logic.services.ServiceFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("services")
public class ServiceResource
{


    private static Gson                          gson   = SpecializedGson.create();
    private static ServiceFacade<JpaTransaction> facade = Facades.services;

    private static AuthenticationFacade authenticationFacade = Facades.authentication;

    @POST
    @Path("register")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response register(String json) throws ResourceConflictException
    {
        RegisterRequest  registerRequest = gson.fromJson(json, RegisterRequest.class);
        Service          service         = facade.register(registerRequest.name, registerRequest.password);
        RegisterResponse response        = new RegisterResponse();
        response.id = service.getId();
        response.name = service.getName();
        response.status = service.getStatus();
        return Response.status(CREATED).entity(gson.toJson(response)).build();
    }

    private class RegisterRequest
    {
        public String name;
        public String password;
    }

    private class RegisterResponse
    {
        public int            id;
        public String         name;
        public Service.Status status;
    }

    @GET
    @Path("request/{request}")
    @Produces
    public Response getRequest(@PathParam("request") String request) throws ResourceNotFoundException
    {
        return Response.ok(gson.toJson(facade.getRequest(request))).build();
    }

    @POST
    @Path("authenticate-service")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response authenticateService(String json) throws Exception
    {
        AuthenticationRequest  authenticationRequest = gson.fromJson(json, AuthenticationRequest.class);
        AuthenticationContext  authenticationContext = facade.authenticateService(authenticationRequest.name, authenticationRequest.password);
        String                 token                 = authenticationFacade.generateAuthenticationToken(authenticationContext);
        AuthenticationResponse response              = new AuthenticationResponse();
        response.token = token;
        response.service = authenticationContext.getService();

        return Response.ok(gson.toJson(response)).build();
    }

    private static class AuthenticationRequest
    {
        public String name;
        public String password;
    }

    private static class AuthenticationResponse
    {
        public Service service;
        public String  token;
    }

    @POST
    @Path("templates")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createTemplate(@HeaderParam("Authorization") String auth, String json)
    throws AuthenticationException, ResourceConflictException, AuthorizationException
    {
        TemplateRequest       request               = gson.fromJson(json, TemplateRequest.class);
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        PermissionTemplate template = facade.createTemplate(authenticationContext,
                                                            request.name,
                                                            request.message,
                                                            request.permissions);
        return Response.status(CREATED).entity(gson.toJson(template)).build();
    }

    private class TemplateRequest
    {
        public String       name;
        public String       message;
        public List<String> permissions;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("templates")
    public Response getTemplates(@HeaderParam("Authorization") String authToken)
    throws AuthorizationException, AuthenticationException
    {
        AuthenticationContext    auth                = authenticationFacade.authenticateBearerHeader(authToken);
        List<PermissionTemplate> permissionTemplates = facade.getTemplates(auth);
        List<PermissionTemplateTransfer> transfers = permissionTemplates.stream()
                                                                        .map(p -> new PermissionTemplateTransfer(p, false))
                                                                        .collect(Collectors.toList());
        return Response.ok(gson.toJson(transfers)).build();
    }

    @GET
    @Path("templates/{id}")
    @Produces(APPLICATION_JSON)
    public Response getTemplate(@HeaderParam("Authorization") String authToken, @PathParam("id") String id)
    throws ResourceNotFoundException, AuthorizationException, AuthenticationException
    {
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(authToken);
        PermissionTemplate    permissionTemplate    = facade.getTemplate(authenticationContext, id);
        return Response.ok(gson.toJson(permissionTemplate)).build();
    }

    @GET
    @Path("templates/name/{name}")
    @Produces(APPLICATION_JSON)
    public Response getTemplateByName(@HeaderParam("Authorization") String authToken, @PathParam("name") String name)
    throws ResourceNotFoundException, AuthorizationException, AuthenticationException
    {
        AuthenticationContext auth               = authenticationFacade.authenticateBearerHeader(authToken);
        PermissionTemplate    permissionTemplate = facade.getTemplateByName(auth, name);
        return Response.ok(gson.toJson(permissionTemplate)).build();
    }

    @POST
    @Path("request-auth")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response requestAuth(@HeaderParam("Authorization") String auth, String json)
    throws AuthenticationException, AuthorizationException, ResourceNotFoundException
    {
        AuthRequestRequest    posted                = gson.fromJson(json, AuthRequestRequest.class);
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        AuthRequest           created               = facade.requestAuth(authenticationContext, posted.callback, posted.template);
        return Response.status(CREATED).entity(gson.toJson(created)).build();
    }

    public class AuthRequestRequest
    {
        public String callback;
        public String template;
    }

    @POST
    @Path("authenticate-user")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response authenticateUser(String json) throws ResourceNotFoundException,
                                                         AuthenticationException,
                                                         JwtGenerationException,
                                                         ResourceConflictException
    {
        AuthenticateUserRequest request = gson.fromJson(json, AuthenticateUserRequest.class);
        AuthenticationContext authenticationContext = facade.authenticateServiceUser(
                request.request,
                request.email,
                request.password
        );

        try {

            String     token      = authenticationFacade.generateAuthenticationToken(authenticationContext);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("service", authenticationContext.getServiceId());
            jsonObject.addProperty("user", authenticationContext.getUserId());
            jsonObject.addProperty("token", token);
            return Response.ok(gson.toJson(jsonObject)).build();
        } catch (JwtGenerationException e) {
            throw new AuthenticationException("Could not generate JWT.", e);
        }
    }

    private class AuthenticateUserRequest
    {
        public String request;
        public String email;
        public String password;
    }
}
