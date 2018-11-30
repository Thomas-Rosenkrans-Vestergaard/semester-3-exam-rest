package com.group3.sem3exam.rest.services;

import com.group3.sem3exam.data.services.Service;

public class ServiceDTO
{
    public final Integer        id;
    public final String         name;
    public final Service.Status status;

    private ServiceDTO(Service service)
    {
        this.id = service.getId();
        this.name = service.getName();
        this.status = service.getStatus();
    }

    public static ServiceDTO basic(Service service)
    {
        return new ServiceDTO(service);
    }
}
