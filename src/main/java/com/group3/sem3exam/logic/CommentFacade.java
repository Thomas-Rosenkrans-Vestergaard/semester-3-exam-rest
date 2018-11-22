package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.repositories.CommentRepository;
import com.group3.sem3exam.data.repositories.PostRepository;

import java.util.List;
import java.util.function.Supplier;

public class CommentFacade
{

    private final Supplier<CommentRepository> commentRepositoryFactory;

    /**
     * Creates a new {@link CommentFacade}.
     *
     * @param commentRepositoryFactory The factory that produces country repositories used by this facade.
     */

    public CommentFacade(Supplier<CommentRepository> commentRepositoryFactory)
    {
        this.commentRepositoryFactory = commentRepositoryFactory;
    }

    /**
     * Returns the country with the provided id.
     *
     * @param id The id of the country to return.
     * @return The country with the provided id.
     * @throws ResourceNotFoundException When a country with the provided id does not exist.
     */
    public List<Comment> get(Integer id) throws ResourceNotFoundException
    {
        try (PostRepository pr = commentRepositoryFactory.apply(transactionFactory.get())) {
            return pr.getTimelinePosts(user, pageSize, cutoff);
        }
    }
}
