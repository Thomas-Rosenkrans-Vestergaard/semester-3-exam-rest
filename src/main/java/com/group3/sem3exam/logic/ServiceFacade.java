package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.data.services.*;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authentication.AuthenticationException;
import com.group3.sem3exam.logic.authentication.IncorrectCredentialsException;
import com.group3.sem3exam.logic.authentication.LazyAuthenticationContext;
import com.group3.sem3exam.logic.authorization.AuthorizationException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ServiceFacade<T extends Transaction>
{

    private final Supplier<T>                                transactionFactory;
    private final Function<T, ServiceRepository>             serviceRepositoryFactory;
    private final Function<T, ServiceAuthRequestRepository>  requestRepositoryFactory;
    private final Function<T, ServiceAuthTemplateRepository> templateRepositoryFactory;
    private final Function<T, UserRepository>                userRepositoryFactory;

    public ServiceFacade(
            Supplier<T> transactionFactory,
            Function<T, ServiceRepository> serviceRepositoryFactory,
            Function<T, ServiceAuthRequestRepository> requestRepositoryFactory,
            Function<T, ServiceAuthTemplateRepository> templateRepositoryFactory,
            Function<T, UserRepository> userRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.serviceRepositoryFactory = serviceRepositoryFactory;
        this.requestRepositoryFactory = requestRepositoryFactory;
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

    public ServiceAuthTemplate createTemplate(AuthenticationContext service, String message, List<String> privileges)
    {
        try (ServiceAuthTemplateRepository tr = templateRepositoryFactory.apply(transactionFactory.get())) {
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
    private List<ServicePrivilege> toEnum(List<String> strings)
    {
        List<ServicePrivilege> enums = new ArrayList<>(strings.size());
        for (String string : strings) {
            try {
                enums.add(ServicePrivilege.valueOf(string));
            } catch (IllegalArgumentException e) {
            }
        }

        return enums;
    }

    /**
     * Requests privileges from the provided user.
     *
     * @param service  The service requesting privileges from the user.
     * @param template The id of the template containing the .
     * @param user     The user to request authorization from.
     * @param callback The callback to call when the user authenticates.
     * @return The service auth request that was created.
     * @throws ResourceNotFoundException When the provided request template does not exist.
     * @throws AuthorizationException    When the provided template is not owned by the provided service.
     */
    public ServicePrivilegeRequest request(
            AuthenticationContext service,
            AuthenticationContext user,
            Integer template,
            String callback)
    throws ResourceNotFoundException, AuthorizationException
    {
        try (T transaction = transactionFactory.get()) {
            ServiceAuthRequestRepository  requestRepository  = requestRepositoryFactory.apply(transaction);
            ServiceAuthTemplateRepository templateRepository = templateRepositoryFactory.apply(transaction);

            ServiceAuthTemplate fetchedTemplate = templateRepository.get(template);
            if (fetchedTemplate == null)
                throw new ResourceNotFoundException(ServiceAuthTemplate.class, template);
            if (service.getService().getId() != fetchedTemplate.getService().getId())
                throw new AuthorizationException("Your service does not own that template.");

            return requestRepository.request(fetchedTemplate, user.getUser());
        }
    }

    /**
     * Authenticates a user on behalf of a service authentication request.
     *
     * @param service  The service to authenticate on behalf of.
     * @param email    The email address of the user attempting to authenticate.
     * @param password The password of the user attempting to authenticate.
     * @return The authentication context.
     * @throws ResourceNotFoundException When the provided service does not exist.
     * @throws AuthenticationException   When the user cannot be authenticated.
     */
    public AuthenticationContext authenticateServiceUser(
            Integer service,
            String email,
            String password)
    throws AuthenticationException, ResourceNotFoundException
    {

    }
}
