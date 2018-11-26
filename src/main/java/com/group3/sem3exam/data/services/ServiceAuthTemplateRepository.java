package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.ReadRepository;

import java.util.List;

public interface ServiceAuthTemplateRepository extends ReadRepository<ServiceAuthTemplate, Integer>
{

    /**
     * Create a new service request template.
     *
     * @param message    The message to display to the user.
     * @param privileges The privileges the service is asking for.
     * @param service    The service requesting the privileges.
     * @return The newly creates {@link ServiceAuthTemplate}.
     */
    ServiceAuthTemplate create(String message, List<ServicePrivilege> privileges, Service service);
}
