package com.group3.sem3exam.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group3.sem3exam.data.repositories.JpaUserRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.*;
import com.group3.sem3exam.logic.AuthenticationFacade;
import com.group3.sem3exam.logic.ResourceConflictException;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.ServiceFacade;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.jwt.JwtGenerationException;
import com.group3.sem3exam.logic.authorization.AuthorizationException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("services")
public class ServiceResource
{


    private static Gson                          gson   = SpecializedGson.create();
    private static ServiceFacade<JpaTransaction> facade = new ServiceFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaServiceRepository(transaction),
            transaction -> new JpaAuthRequestRepository(transaction),
            transaction -> new JpaPermissionRequestRepository(transaction),
            transaction -> new JpaPermissionTemplateRepository(transaction),
            transaction -> new JpaUserRepository(transaction),
            transaction -> new JpaPermissionRepository(transaction)
    );

    private static AuthenticationFacade authenticationFacade = Facades.authentication;

    @POST
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
        return Response.status(Response.Status.CREATED).entity(gson.toJson(response)).build();
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

    @POST
    @Path("authentication-service")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response authenticate(String json) throws Exception
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
    @Path("template")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createTemplate(@HeaderParam("Authorization") String auth, String json) throws AuthenticationException
    {
        TemplateRequest       request               = gson.fromJson(json, TemplateRequest.class);
        AuthenticationContext authenticationContext = authenticationFacade.authenticateBearerHeader(auth);
        PermissionTemplate template = facade.createTemplate(authenticationContext,
                                                            request.message,
                                                            request.permissions);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(template)).build();
    }

    private class TemplateRequest
    {
        public String       message;
        public List<String> permissions;
    }

    @POST
    @Path("template/{id}")
    @Produces(APPLICATION_JSON)
    public Response getTemplate(@HeaderParam("Authorization") String authToken, @PathParam("id") String id)
    throws ResourceNotFoundException, AuthorizationException, AuthenticationException
    {
        AuthenticationContext auth               = authenticationFacade.authenticateBearerHeader(authToken);
        PermissionTemplate    permissionTemplate = facade.getTemplate(auth, id);
        if (permissionTemplate == null)
            throw new ResourceNotFoundException(PermissionTemplate.class, id);

        return Response.status(Response.Status.CREATED).entity(gson.toJson(permissionTemplate)).build();
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
        AuthRequest           template              = facade.requestAuth(authenticationContext, posted.callback, posted.template);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(template)).build();
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
    public Response authenticateUser(String json) throws ResourceNotFoundException, AuthenticationException
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

    @POST
    @Path("callback")
    @Consumes(APPLICATION_JSON)
    public void callback(String json)
    {
        System.out.println(json);
    }
}
