package com.group3.sem3exam.logic.authorization;

import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.services.entities.Permission;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;

import static com.group3.sem3exam.logic.authentication.AuthenticationType.SERVICE;
import static com.group3.sem3exam.logic.authentication.AuthenticationType.SERVICE_REPRESENTING_USER;
import static com.group3.sem3exam.logic.authentication.AuthenticationType.USER;

public class CanPerformOnPost implements AuthorizationCheck
{

    private final Post post;
    private final Permission[] required;

    public CanPerformOnPost(Post post, Permission... required)
    {
        this.post = post;
        this.required = required;
    }

    @Override
    public void check(AuthenticationContext authenticationContext) throws AuthorizationException
    {
        if(authenticationContext.getType() == USER){
            if(authenticationContext.getUser().getId() == this.post.getAuthor().getId()){
               return;
            }
        }

        if (authenticationContext.getType() == SERVICE)
            throw new AuthorizationException("You must be a user using a service to access this endpoint.");

        if(authenticationContext.getType() == SERVICE_REPRESENTING_USER){

        }
    }
}
