package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.entities.Comment;
import com.group3.sem3exam.data.entities.CommentParent;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.CommentRepository;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;

import java.util.List;
import java.util.function.Supplier;

public class CommentFacade
{

    /**
     * The supplier that creates comment repositories used by this facade.
     */
    private final Supplier<CommentRepository> commentRepositoryFactory;

    /**
     * Creates a new comment facade.
     *
     * @param commentRepositoryFactory The supplier that creates comment repositories used by this facade.
     */
    public CommentFacade(Supplier<CommentRepository> commentRepositoryFactory)
    {
        this.commentRepositoryFactory = commentRepositoryFactory;
    }

    /**
     * Returns the comments on the comment parent with the provided id.
     *
     * @param auth   The authentication context of the caller.
     * @param parent The comment parent to return the comments of.
     * @return The comments on the comment parent with the provided id.
     * @throws ResourceNotFoundException When the comment parent with the provided id does not exist.
     */
    public List<Comment> getComments(AuthenticationContext auth, Integer parent) throws ResourceNotFoundException
    {
        try (CommentRepository commentRepository = commentRepositoryFactory.get()) {
            CommentParent fetchedParent = commentRepository.getParent(parent);
            if (fetchedParent == null)
                throw new ResourceNotFoundException(CommentParent.class, parent);

            return commentRepository.getAll(fetchedParent);
        }
    }

    /**
     * Returns a paginated view of the comments on the comment parent with the provided id.
     *
     * @param auth       The authentication context of the caller.
     * @param parent     The comment parent to return the comments of.
     * @param pageSize   The number of results on a single page, where {@code pageSize >= 0}.
     * @param pageNumber The position of the page to retrieve, where {@code pageNumber >= 1}.
     * @return The comments on the comment parent with the provided id.
     * @throws ResourceNotFoundException When the comment parent with the provided id does not exist.
     */
    public List<Comment> getCommentsPage(AuthenticationContext auth, Integer parent, Integer pageSize, Integer pageNumber)
    throws ResourceNotFoundException
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        try (CommentRepository commentRepository = commentRepositoryFactory.get()) {
            CommentParent fetchedParent = commentRepository.getParent(parent);
            if (fetchedParent == null)
                throw new ResourceNotFoundException(CommentParent.class, parent);

            return commentRepository.getPaginated(fetchedParent, pageSize, pageNumber);
        }
    }

    /**
     * Creates a new comment, attaching it to the provided comment parent.
     *
     * @param auth     The authentication context of the actor creating the comment.
     * @param contents The contents of the comment.
     * @param parent   The comment parent to attach the comment to.
     * @return The newly created comment.
     * @throws ResourceNotFoundException
     */
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

    /**
     * Returns the number of comments on the parent.
     *
     * @param parent The parent to count the number of comments on.
     * @return The number of comments on the parent.
     * @throws ResourceNotFoundException When a comment parent with the provided id does not exist.
     */
    public int count(Integer parent) throws ResourceNotFoundException
    {
        try (CommentRepository commentRepository = commentRepositoryFactory.get()) {
            CommentParent fetchedParent = commentRepository.getParent(parent);
            if (fetchedParent == null)
                throw new ResourceNotFoundException(CommentParent.class, parent);
            return commentRepository.count(fetchedParent);
        }
    }

    public Comment delete(AuthenticationContext auth, Integer comment) throws ResourceNotFoundException
    {
        try(CommentRepository commentRepository = commentRepositoryFactory.get()){
            commentRepository.begin();
            User user = auth.getUser();
            Comment commentToDelete = commentRepository.get(comment);
            if(commentToDelete.getAuthor() != user){
                throw new ResourceNotFoundException(Comment.class, user);
            }
            if(commentToDelete == null){
                throw new ResourceNotFoundException(Comment.class, comment);
            }
            commentRepository.delete(commentToDelete);
            commentRepository.commit();
            return commentToDelete;
        }
    }

}
