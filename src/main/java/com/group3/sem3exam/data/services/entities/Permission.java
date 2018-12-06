package com.group3.sem3exam.data.services.entities;

public enum Permission
{
    CREATE_POST("Create post", "The ability to create posts."),
    DELETE_POST("Delete post", "The ability to delete posts."),
    UPDATE_POST("Update post", "The ability to update posts.");

    private String naturalName;
    private String description;

    Permission(String naturalName, String description)
    {
        this.naturalName = naturalName;
        this.description = description;
    }

    public String getNaturalName()
    {
        return this.naturalName;
    }

    public String getDescription()
    {
        return this.description;
    }
}
