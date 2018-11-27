package com.group3.sem3exam.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.data.services.*;
import com.group3.sem3exam.logic.authentication.*;
import com.group3.sem3exam.logic.authorization.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ServiceFacade<T extends Transaction>
{

    private final Supplier<T>                               transactionFactory;
    private final Function<T, ServiceRepository>            serviceRepositoryFactory;
    private final Function<T, AuthRequestRepository>        requestRepositoryFactory;
    private final Function<T, PermissionRequestRepository>  permissionRequestRepositoryFactory;
    private final Function<T, PermissionTemplateRepository> templateRepositoryFactory;
    private final Function<T, UserRepository>               userRepositoryFactory;

    public ServiceFacade(
            Supplier<T> transactionFactory,
            Function<T, ServiceRepository> serviceRepositoryFactory,
            Function<T, AuthRequestRepository> authRequestRepositoryFactory,
            Function<T, PermissionRequestRepository> permissionRequestRepositoryFactory,
            Function<T, PermissionTemplateRepository> templateRepositoryFactory,
            Function<T, UserRepository> userRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.serviceRepositoryFactory = serviceRepositoryFactory;
        this.requestRepositoryFactory = authRequestRepositoryFactory;
        this.permissionRequestRepositoryFactory = permissionRequestRepositoryFactory;
        this.templateRepositoryFactory = templateRepositoryFactory;
        this.userRepositoryFactory = userRepositoryFactory;
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

            return LazyAuthenticationContext.service(service);
        }
    }

    /**
     * Registers a new service account.
     *
     * @param name     The name of the service account.
     * @param password The password of the service account
     * @return The newly created service.
     */
    public Service register(String name, String password)
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            ServiceRepository serviceRepository = serviceRepositoryFactory.apply(transaction);
            Service           service           = serviceRepository.create(name, hash(password));
            transaction.commit();
            return service;
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

    public PermissionTemplate createTemplate(AuthenticationContext service, String message, List<String> privileges)
    {
        try (PermissionTemplateRepository tr = templateRepositoryFactory.apply(transactionFactory.get())) {
            return tr.create(message, toEnum(privileges), service.getService());
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
     * @param timeout  The number of minutes to wait before timing out the request. Default is 5.
     * @return The service auth request that was created.
     * @throws AuthorizationException When the provided auth context is not a service.
     */
    public AuthRequest requestAuth(AuthenticationContext service, String callback, Integer timeout)
    throws AuthorizationException
    {
        timeout = timeout == null ? 5 : timeout;

        new Authorizator(service).check(new IsService());

        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            AuthRequestRepository requestRepository = requestRepositoryFactory.apply(transaction);
            AuthRequest           authRequest       = requestRepository.create(service.getService(), callback, timeout);
            transaction.commit();
            return authRequest;
        }
    }

    /**
     * Returns the template with the provided id.
     *
     * @param service The service requesting privileges from the user.
     * @param id      The id of the template to return.
     * @return The template with the provided id.
     * @throws AuthorizationException    When the provided auth context is not a service.
     * @throws AuthorizationException    When the template with the provided id is not owned by the service.
     * @throws ResourceNotFoundException When the template with the provided id does not exist.
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
     */
    public AuthenticationContext authenticateServiceUser(
            String request,
            String email,
            String password)
    throws ResourceNotFoundException, AuthenticationException
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            AuthRequestRepository requestRepository = requestRepositoryFactory.apply(transaction);
            AuthRequest           authRequest       = requestRepository.get(request);
            if (authRequest == null)
                throw new ResourceNotFoundException(AuthRequest.class, request);
            UserAuthenticator     userAuthenticator = new UserAuthenticator(() -> userRepositoryFactory.apply(transaction));
            AuthenticationContext userContext       = userAuthenticator.authenticate(email, password);

            try {
                requestRepository.completed(authRequest);
                notifyServiceOnAuth(authRequest, userContext.getUser());
                transaction.commit();
            } catch (IOException e) {
                transaction.rollback();
                e.printStackTrace();
            }

            return LazyAuthenticationContext.representing(authRequest.getService(), userContext.getUser());
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
    private void notifyServiceOnAuth(AuthRequest request, User user) throws IOException
    {
        AuthenticationNotifyTransfer transfer = new AuthenticationNotifyTransfer();
        transfer.request = request.getId();
        transfer.user = new UserTransfer(user);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        post(request.getCallback(), gson.toJson(transfer));
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
        URL               url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("User-Agent", "Server");

        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(contents.getBytes("UTF-8"));
        }
    }

    /**
     * The response to send to the service when a user successfully authenticates
     * as a response to a authentication request.
     */
    private class AuthenticationNotifyTransfer
    {
        private String       request;
        private UserTransfer user;
    }

    /**
     * The view of the authenticated user sent to the service, when the user
     * successfully authenticates as a response to a authentication request.
     */
    private class UserTransfer
    {
        private Integer       id;
        private String        name;
        private String        email;
        private LocalDateTime createdAt;

        public UserTransfer(User user)
        {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt();
        }
    }

    /**
     * Requests permissions from the provided {@code serviceUser}.
     *
     * @param serviceUser The user to request permissions from.
     * @param template    The permissions to request.
     * @return The newly created permission request.
     * @throws ResourceNotFoundException When the provided template does not exist.
     * @throws ResourceAccessException   When the provided template does not belong to the service.
     * @throws AuthorizationException    When the provided authentication context is not a service user.
     */
    public PermissionRequest requestPermissions(AuthenticationContext serviceUser, String template)
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
                    fetchedTemplate
            );

            transaction.commit();

            return permissionRequest;
        }
    }

    public PermissionRequest rejectPermissionRequest(AuthenticationContext user, String request)
    {
        return updatePermissionRequest(user, request, PermissionRequest.Status.REJECTED);
    }

    public PermissionRequest acceptPermissionRequest(AuthenticationContext user, String request)
    {
        return updatePermissionRequest(user, request, PermissionRequest.Status.ACCEPTED);
    }

    /**
     * Updates the state of the the provided permission request.
     *
     * @param user     The owner of the request.
     * @param request  The request.
     * @param response The response to the request.
     * @return The updated permission request.
     * @throws ResourceNotFoundException When the provided request does not exist.
     * @throws ResourceAccessException   When the provided request is not owned by the provided user.
     * @throws ResourceConflictException When the provided request has already been set.
     */
    private PermissionRequest updatePermissionRequest(
            AuthenticationContext user,
            String request,
            PermissionRequest.Status response)
    throws ResourceNotFoundException, ResourceAccessException, ResourceConflictException
    {

    }
}
