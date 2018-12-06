package com.group3.sem3exam.logic.services;

import com.google.gson.Gson;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.data.services.*;
import com.group3.sem3exam.data.services.entities.*;
import com.group3.sem3exam.logic.ResourceConflictException;
import com.group3.sem3exam.logic.ResourceNotFoundException;
import com.group3.sem3exam.logic.SpecializedGson;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.IncorrectCredentialsException;
import com.group3.sem3exam.logic.authentication.UserAuthenticator;
import com.group3.sem3exam.logic.authentication.jwt.JwtGenerationException;
import com.group3.sem3exam.logic.authentication.jwt.JwtSecret;
import com.group3.sem3exam.logic.authentication.jwt.JwtTokenGenerator;
import com.group3.sem3exam.logic.authorization.*;
import com.group3.sem3exam.logic.validation.ResourceValidationException;
import com.group3.sem3exam.logic.validation.ResourceValidator;
import net.sf.oval.constraint.AssertURL;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Size;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.group3.sem3exam.data.services.entities.PermissionRequest.Status.ACCEPTED;
import static com.group3.sem3exam.logic.authentication.LazyAuthenticationContext.service;
import static com.group3.sem3exam.logic.authentication.LazyAuthenticationContext.serviceUser;

public class ServiceFacade<T extends Transaction>
{

    private final Supplier<T>                               transactionFactory;
    private final Function<T, ServiceRepository>            serviceRepositoryFactory;
    private final Function<T, AuthRequestRepository>        requestRepositoryFactory;
    private final Function<T, PermissionRequestRepository>  permissionRequestRepositoryFactory;
    private final Function<T, PermissionTemplateRepository> templateRepositoryFactory;
    private final Function<T, UserRepository>               userRepositoryFactory;
    private final Function<T, PermissionRepository>         permissionRepositoryFactory;
    private final JwtSecret                                 jwtSecret;

    public ServiceFacade(
            Supplier<T> transactionFactory,
            Function<T, ServiceRepository> serviceRepositoryFactory,
            Function<T, AuthRequestRepository> authRequestRepositoryFactory,
            Function<T, PermissionRequestRepository> permissionRequestRepositoryFactory,
            Function<T, PermissionTemplateRepository> templateRepositoryFactory,
            Function<T, UserRepository> userRepositoryFactory,
            Function<T, PermissionRepository> permissionRepositoryFactory,
            JwtSecret jwtSecret)
    {
        this.transactionFactory = transactionFactory;
        this.serviceRepositoryFactory = serviceRepositoryFactory;
        this.requestRepositoryFactory = authRequestRepositoryFactory;
        this.permissionRequestRepositoryFactory = permissionRequestRepositoryFactory;
        this.templateRepositoryFactory = templateRepositoryFactory;
        this.userRepositoryFactory = userRepositoryFactory;
        this.permissionRepositoryFactory = permissionRepositoryFactory;
        this.jwtSecret = jwtSecret;
    }

    /**
     * Authenticates a service using the provided name and password.
     *
     * @param name     The name of the service.
     * @param password The password.
     * @return The authentication context.
     * @throws AuthenticationException When the service could not be authenticated.
     */
    public AuthenticationContext authenticateService(String name, String password) throws AuthenticationException
    {
        try (ServiceRepository sr = serviceRepositoryFactory.apply(transactionFactory.get())) {
            Service service = sr.getByName(name);
            if (service == null)
                throw new AuthenticationException(new IncorrectCredentialsException());
            if (!check(password, service.getPasswordHash()))
                throw new AuthenticationException(new IncorrectCredentialsException());

            return service(service);
        }
    }

    /**
     * Registers a new service account.
     *
     * @param name     The name of the service account.
     * @param password The password of the service account
     * @return The newly created service.
     * @throws ResourceValidationException When the provided information is invalid.
     * @throws ResourceConflictException   When a service account with the provided name already exists.
     */
    public Service register(String name, String password) throws ResourceValidationException,
                                                                 ResourceConflictException
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();

            ServiceValidator                    validatorObject = new ServiceValidator(name, password);
            ResourceValidator<ServiceValidator> validator       = new ResourceValidator<>(validatorObject);
            validator.oval();
            if (validator.hasErrors())
                validator.throwResourceValidationException();

            ServiceRepository serviceRepository = serviceRepositoryFactory.apply(transaction);
            if (serviceRepository.getByName(name) != null)
                throw new ResourceConflictException(Service.class, "A service account with the provided name exists.");

            Service service = serviceRepository.create(name, hash(password));
            service = serviceRepository.enable(service);
            transaction.commit();
            return service;
        }
    }

    private class ServiceValidator
    {

        @NotNull
        @Length(min = 6, max = 255)
        private String name;

        @NotNull
        @Length(min = 4)
        private String password;

        public ServiceValidator(String name, String password)
        {
            this.name = name;
            this.password = password;
        }
    }

    /**
     * Hashes the provided password.
     *
     * @param password The password to hash.
     * @return The hashed password.
     */
    private String hash(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Checks that the provided {@code password} matches the provided {@code hash} using the b-crypt algorithm.
     *
     * @param password The plaintext password.
     * @param hash     The b-crypt hash to check against.
     * @return {@code true} when the  provided {@code password} matches the provided {@code hash}, {@code false}
     * otherwise.
     */
    private boolean check(String password, String hash)
    {
        return BCrypt.checkpw(password, hash);
    }

    /**
     * Creates a new permission template.
     * The template contains information about the permissions required to perform some operation.
     *
     * @param service     The service creating the template.
     * @param name        The name of the template. The name is unique for a given service.
     * @param message     The message to display to the user when they are
     * @param permissions The permissions required by the service.
     * @return The newly created template.
     * @throws AuthorizationException      When the provided auth context is not a service.
     * @throws ResourceConflictException   When a template with the same name already exists for the provided service.
     * @throws ResourceValidationException When
     */
    public PermissionTemplate createTemplate(AuthenticationContext service, String name, String message, List<String> permissions)
    throws AuthorizationException, ResourceConflictException, ResourceValidationException
    {
        new Authorizator(service).check(new IsService());

        ResourceValidator<PermissionTemplateValidator> validator =
                new ResourceValidator<>(new PermissionTemplateValidator(name, message, permissions));

        validator.oval();
        if (validator.hasErrors())
            validator.throwResourceValidationException();

        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            PermissionTemplateRepository tr = templateRepositoryFactory.apply(transaction);
            if (tr.getByName(service.getService(), name) != null)
                throw new ResourceConflictException(PermissionTemplate.class, "A template with that name already exists.");
            PermissionTemplate template = tr.create(name, message, toEnum(permissions), service.getService());
            transaction.commit();
            return template;
        }
    }

    private class PermissionTemplateValidator
    {

        @Length(min = 1, max = 255)
        @NotNull
        private String name;

        @Length(min = 1, max = 255)
        @NotNull
        private String message;

        @Size(min = 1, max = 255)
        private List<String> permissions;

        public PermissionTemplateValidator(String name, String message, List<String> permissions)
        {
            this.name = name;
            this.message = message;
            this.permissions = permissions;
        }
    }

    /**
     * Converts the provided list of privilege names to a list of enums. When a string cannot be mapped to an
     * enum value, that string is ignored.
     *
     * @param strings The strings to convert.
     * @return The list of enums.
     */
    private List<Permission> toEnum(List<String> strings)
    {
        List<Permission> enums = new ArrayList<>(strings.size());
        for (String string : strings) {
            try {
                enums.add(Permission.valueOf(string));
            } catch (IllegalArgumentException e) {
            }
        }

        return enums;
    }

    /**
     * Requests privileges from the provided user.
     *
     * @param service  The service requesting privileges from the user.
     * @param callback The callback to call when the user authenticates.
     * @param template An optional template that should also be accepted when the user authenticate. Can be {@code null}.
     * @return The service auth request that was created.
     * @throws AuthorizationException      When the provided auth context is not a service.
     * @throws ResourceNotFoundException   When the provided template cannot be found.
     * @throws ResourceValidationException When the provided information is invalid.
     */
    public AuthRequest requestAuth(AuthenticationContext service, String callback, String template)
    throws AuthorizationException, ResourceNotFoundException, ResourceValidationException
    {
        new Authorizator(service).check(new IsService());

        AuthRequestValidator                    validatorObject   = new AuthRequestValidator(callback, template);
        ResourceValidator<AuthRequestValidator> resourceValidator = new ResourceValidator(validatorObject);
        resourceValidator.oval();
        if (resourceValidator.hasErrors())
            resourceValidator.throwResourceValidationException();


        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            AuthRequestRepository        requestRepository  = requestRepositoryFactory.apply(transaction);
            PermissionTemplateRepository templateRepository = templateRepositoryFactory.apply(transaction);
            PermissionTemplate           fetchedTemplate    = templateRepository.get(template);
            if (template != null && fetchedTemplate == null)
                throw new ResourceNotFoundException(PermissionTemplate.class, template);
            if (template != null && fetchedTemplate.getService().getId() != service.getServiceId())
                throw new ResourceAccessException(PermissionTemplate.class, template);

            AuthRequest authRequest = requestRepository.create(service.getService(), callback, fetchedTemplate);
            authRequest.getTemplate().getPermissionMappings().size();
            transaction.commit();
            return authRequest;
        }
    }

    private class AuthRequestValidator
    {
        @NotNull
        @Length(min = 1, max = 255)
        @AssertURL
        private String callback;

        @Length(max = 255)
        private String template;

        public AuthRequestValidator(String callback, String template)
        {
            this.callback = callback;
            this.template = template;
        }
    }

    /**
     * Returns the template with the provided id.
     *
     * @param service The service retrieving the template.
     * @param id      The id of the template to return.
     * @return The template with the provided id.
     * @throws AuthorizationException    When the provided auth context is not a service.
     * @throws AuthorizationException    When the template with the provided id is not owned by the service.
     * @throws ResourceNotFoundException When the template with the provided id does not exist.
     * @throws ResourceAccessException   When the provided template is not owned by the service making the request.
     */
    public PermissionTemplate getTemplate(AuthenticationContext service, String id)
    throws ResourceNotFoundException, AuthorizationException
    {
        new Authorizator(service).check(new IsService());

        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            PermissionTemplateRepository templateRepository = templateRepositoryFactory.apply(transaction);
            PermissionTemplate           template           = templateRepository.get(id);
            if (template == null)
                throw new ResourceNotFoundException(PermissionTemplate.class, id);
            if (template.getService().getId() != service.getServiceId())
                throw new ResourceAccessException(PermissionTemplate.class, id);

            transaction.commit();
            return template;
        }
    }

    /**
     * Authenticates a user on behalf of a service authentication request.
     *
     * @param request  The id of the request to the user is authenticating for.
     * @param email    The email address of the user attempting to authenticate.
     * @param password The password of the user attempting to authenticate.
     * @return The authentication context.
     * @throws ResourceNotFoundException When the request does not exist.
     * @throws AuthenticationException   When the user cannot be authenticated.
     * @throws ResourceConflictException When the provided request has already been used.
     */
    public AuthenticationContext authenticateServiceUser(
            String request,
            String email,
            String password)
    throws ResourceNotFoundException, AuthenticationException, JwtGenerationException, ResourceConflictException
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            AuthRequestRepository requestRepository = requestRepositoryFactory.apply(transaction);
            AuthRequest           authRequest       = requestRepository.get(request);
            if (authRequest == null)
                throw new ResourceNotFoundException(AuthRequest.class, request);
            if (authRequest.getStatus() != AuthRequest.Status.READY)
                throw new ResourceConflictException(AuthRequest.class, "The authentication request has already been used.");

            UserAuthenticator userAuthenticator = new UserAuthenticator(
                    () -> userRepositoryFactory.apply(transactionFactory.get())); // Use new transaction
            AuthenticationContext userContext = userAuthenticator.authenticate(email, password);

            // We should also allow the template.
            if (authRequest.getTemplate() != null) {
                PermissionRequestRepository permissionRequestRepository =
                        permissionRequestRepositoryFactory.apply(transaction);

                PermissionRequest permissionRequest = permissionRequestRepository.create(
                        userContext.getUser(),
                        "",
                        authRequest.getTemplate());
                permissionRequestRepository.updateStatus(permissionRequest, ACCEPTED);
            }

            PermissionRepository permissionRepository = permissionRepositoryFactory.apply(transaction);
            Service              service              = authRequest.getService();
            User                 user                 = userContext.getUser();
            AuthenticationContext authenticationContext =
                    serviceUser(service, user, permissionRepository.getPermissionsFor(service, user));

            try {
                requestRepository.completed(authRequest);
                notifyServiceOnAuth(authRequest, generateToken(authenticationContext), userContext.getUser());
                transaction.commit();
            } catch (IOException e) {
                transaction.rollback();
                e.printStackTrace();
            }

            return authenticationContext;
        }
    }

    /**
     * Notifies a service that a user has successfully authenticated in response to
     * an authentication request sent to the
     *
     * @param request The request the user authenticated in response to.
     * @param token   The jwt token.
     * @param user    The user who was authenticated.
     * @throws IOException When the connection to the service could not be made.
     */
    private void notifyServiceOnAuth(AuthRequest request, String token, User user) throws IOException
    {
        try (PermissionRepository pr = permissionRepositoryFactory.apply(transactionFactory.get())) {
            AuthResponseTransfer transfer = new AuthResponseTransfer(
                    request,
                    user,
                    token,
                    pr.getPermissionsFor(request.getService(), user)
            );

            Gson gson = SpecializedGson.create();
            post(request.getCallback(), gson.toJson(transfer));
        }
    }

    /**
     * Posts the provided JSON {@code contents} to the provided {@code address}.
     *
     * @param address  The address to post the contents to.
     * @param contents The contents to post to the address.
     * @throws IOException When the post could not be made.
     */
    private void post(String address, String contents) throws IOException
    {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost   request    = new HttpPost(address);
        try {
            StringEntity params = new StringEntity(contents);
            request.addHeader("Content-Type", "application/json");
            request.setEntity(params);
            httpClient.execute(request);
        } finally {
            request.releaseConnection();
        }
    }

    /**
     * Requests permissions from the provided {@code serviceUser}.
     *
     * @param serviceUser The user to request permissions from.
     * @param callback    The endpoint notified when the user updates the permissions of this request.
     * @param template    The permissions to request.
     * @return The newly created permission request.
     * @throws ResourceNotFoundException When the provided template does not exist.
     * @throws ResourceAccessException   When the provided template does not belong to the service.
     * @throws AuthorizationException    When the provided authentication context is not a service user.
     */
    public PermissionRequest requestPermissions(
            AuthenticationContext serviceUser, String callback, String template)
    throws ResourceNotFoundException, AuthorizationException
    {
        new Authorizator(serviceUser).check(new IsServiceUser());

        try (T transaction = transactionFactory.get()) {
            transaction.begin();

            PermissionTemplateRepository templateRepository = templateRepositoryFactory.apply(transaction);

            PermissionTemplate fetchedTemplate = templateRepository.get(template);
            if (fetchedTemplate == null)
                throw new ResourceNotFoundException(PermissionTemplate.class, template);
            if (fetchedTemplate.getService().getId() != serviceUser.getServiceId())
                throw new ResourceAccessException(PermissionTemplate.class, template);

            PermissionRequestRepository requestRepository = permissionRequestRepositoryFactory.apply(transaction);
            PermissionRequest permissionRequest = requestRepository.create(
                    serviceUser.getUser(),
                    callback,
                    fetchedTemplate
            );

            transaction.commit();

            return permissionRequest;
        }
    }

    /**
     * Updates the state of the the provided permission request to be {@link PermissionRequest.Status#REJECTED}.
     *
     * @param user    The owner of the request.
     * @param request The request.
     * @return The updated permission request.
     * @throws AuthorizationException    When the provided auth context is not a user.
     * @throws ResourceNotFoundException When the provided request does not exist.
     * @throws ResourceAccessException   When the provided request is not owned by the provided user.
     */
    public PermissionRequest rejectPermissionRequest(AuthenticationContext user, String request)
    throws ResourceNotFoundException, AuthorizationException
    {
        return updatePermissions(user, request, PermissionRequest.Status.REJECTED);
    }

    /**
     * Updates the state of the the provided permission request to be {@link PermissionRequest.Status#ACCEPTED}.
     *
     * @param user    The owner of the request.
     * @param request The request.
     * @return The updated permission request.
     * @throws AuthorizationException    When the provided auth context is not a user.
     * @throws ResourceNotFoundException When the provided request does not exist.
     * @throws ResourceAccessException   When the provided request is not owned by the provided user.
     */
    public PermissionRequest acceptPermissionRequest(AuthenticationContext user, String request)
    throws ResourceNotFoundException, AuthorizationException
    {
        return updatePermissions(user, request, ACCEPTED);
    }

    /**
     * Updates the state of the the provided permission request.
     *
     * @param user     The owner of the request.
     * @param request  The request.
     * @param response The response to the request.
     * @return The updated permission request.
     * @throws AuthorizationException    When the provided auth context is not a user.
     * @throws ResourceNotFoundException When the provided request does not exist.
     * @throws ResourceAccessException   When the provided request is not owned by the provided user.
     */
    private PermissionRequest updatePermissions(
            AuthenticationContext user,
            String request,
            PermissionRequest.Status response)
    throws ResourceNotFoundException, AuthorizationException
    {
        if (response == PermissionRequest.Status.PENDING)
            throw new IllegalArgumentException();

        new Authorizator(user).check(new IsUser());

        try (T transaction = transactionFactory.get()) {

            transaction.begin();
            PermissionRequestRepository prr               = permissionRequestRepositoryFactory.apply(transaction);
            PermissionRequest           permissionRequest = prr.get(request);

            if (permissionRequest == null)
                throw new ResourceNotFoundException(PermissionRequest.class, request);
            if (permissionRequest.getUser().getId() != user.getUserId())
                throw new ResourceAccessException(PermissionRequest.class, request);

            PermissionRequest updated = prr.updateStatus(permissionRequest, response);

            try {
                notifyServiceOnPermissionChange(permissionRequest, user.getUser());
                transaction.commit();
            } catch (IOException e) {
                transaction.rollback();
                e.printStackTrace();
            }

            return updated;
        }
    }

    /**
     * Notifies a service that a user has successfully authenticated in response to
     * an authentication request sent to the
     *
     * @param request The request the user authenticated in response to.
     * @param user    The user who was authenticated.
     * @throws IOException When the connection to the service could not be made.
     */
    private void notifyServiceOnPermissionChange(PermissionRequest request, User user) throws IOException
    {
        PermissionNotifyTransfer transfer = new PermissionNotifyTransfer();
        transfer.request = request.getId();
        transfer.template = request.getTemplate().getId();
        transfer.status = request.getStatus();
        transfer.permissions = new ArrayList<>();
        transfer.user = new UserTransfer(user);

        Gson gson = SpecializedGson.create();
        post(request.getCallback(), gson.toJson(transfer));
    }

    /**
     * Returns the request with the provided id.
     *
     * @param id The id of the request to return.
     * @return The request with the provided id.
     * @throws ResourceNotFoundException When a request with the provided id does not exist.
     */
    public Object getRequest(String id) throws ResourceNotFoundException
    {
        try (AuthRequestRepository repository = requestRepositoryFactory.apply(transactionFactory.get())) {
            AuthRequest fetched = repository.get(id);
            if (fetched == null)
                throw new ResourceNotFoundException(AuthRequest.class, id);

            return fetched;
        }
    }

    private class PermissionNotifyTransfer
    {
        String                   request;
        String                   template;
        PermissionRequest.Status status;
        List<Permission>         permissions;
        UserTransfer             user;
    }

    /**
     * Returns the permission templates of the provided service.
     *
     * @param service The service to return the permission templates of.
     * @return The permission templates of the provided service.
     * @throws AuthorizationException When the provided authentication context is not a service.
     */
    public List<PermissionTemplate> getTemplates(AuthenticationContext service) throws AuthorizationException
    {
        new Authorizator(service).check(new IsService());

        try (T transaction = transactionFactory.get()) {
            PermissionTemplateRepository templateRepository = templateRepositoryFactory.apply(transaction);
            return templateRepository.getByService(service.getService());
        }
    }

    /**
     * Returns the template owned by the provided service with the provided name.
     *
     * @param service The owner of the templates to search in.
     * @param name    The name of the template to return.
     * @return The template owned by the provided service with the provided name.
     * @throws AuthorizationException    When the provided authentication context is not a service.
     * @throws ResourceNotFoundException When no template with the provided name exist.
     */
    public PermissionTemplate getTemplateByName(AuthenticationContext service, String name)
    throws AuthorizationException, ResourceNotFoundException
    {
        new Authorizator(service).check(new IsService());

        try (PermissionTemplateRepository repository = templateRepositoryFactory.apply(transactionFactory.get())) {
            PermissionTemplate fetched = repository.getByName(service.getService(), name);
            if (fetched == null)
                throw new ResourceNotFoundException(PermissionTemplate.class, name);
            return fetched;
        }
    }

    private String generateToken(AuthenticationContext authenticationContext) throws JwtGenerationException
    {
        JwtTokenGenerator tokenGenerator = new JwtTokenGenerator(jwtSecret);
        return tokenGenerator.generate(authenticationContext);
    }
}
