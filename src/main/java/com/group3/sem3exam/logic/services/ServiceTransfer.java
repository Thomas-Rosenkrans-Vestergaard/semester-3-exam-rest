package com.group3.sem3exam.logic.services;

import com.group3.sem3exam.data.services.Service;

public class ServiceTransfer
{

    public final Integer        id;
    public final String         name;
    public final Service.Status status;

    public ServiceTransfer(Service service)
    {
        this.id = service.getId();
        this.name = service.getName();
        this.status = service.getStatus();
    }
}
