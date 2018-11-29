package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.CommentParent;
import com.group3.sem3exam.data.repositories.CommentRepository;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;

import java.util.List;
import java.util.function.Supplier;

public class CommentFacade
{

    private final Supplier<CommentRepository> commentRepositoryFactory;

    public CommentFacade(Supplier<CommentRepository> commentRepositoryFactory)
    {
        this.commentRepositoryFactory = commentRepositoryFactory;
    }

    public List<Comment> getComments(Integer parent) throws ResourceNotFoundException
    {
        try (CommentRepository commentRepository = commentRepositoryFactory.get()) {
            CommentParent fetchedParent = commentRepository.getParent(parent);
            if (fetchedParent == null)
                throw new ResourceNotFoundException(CommentParent.class, parent);

            return commentRepository.getAll(fetchedParent);
        }
    }

    public Comment create(AuthenticationContext auth, String contents, Integer parent)
    throws ResourceNotFoundException
    {
        try (CommentRepository commentRepository = commentRepositoryFactory.get()) {
            commentRepository.begin();
            CommentParent fetchedParent = commentRepository.getParent(parent);
            if (fetchedParent == null)
                throw new ResourceNotFoundException(CommentParent.class, parent);

            Comment created = commentRepository.create(auth.getUser(), contents, fetchedParent);
            commentRepository.commit();
            return created;
        }
    }
}
