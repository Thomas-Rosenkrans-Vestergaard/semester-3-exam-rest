package com.group3.sem3exam.logic;


import com.group3.sem3exam.data.entities.*;
import com.group3.sem3exam.data.repositories.ImagePostImageRepository;
import com.group3.sem3exam.data.repositories.PostRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.images.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class PostFacade<T extends Transaction>
{

    /**
     * A factory that returns {@link Transaction} instances of type {@code T}.
     */
    private final Supplier<T> transactionFactory;

    /**
     * A factory that creates a new {@link PostRepository} from a provided {@link T}.
     */
    private final Function<T, PostRepository> postRepositoryFactory;

    /**
     * A factory that creates a new {@link UserRepository} from a provided {@link T}.
     */
    private final Function<T, UserRepository>           userRepositoryFactory;
    private final Function<T, ImagePostImageRepository> imagePostImageRepositoryFactory;

    /**
     * Creates a new {@link PostFacade}.
     *
     * @param transactionFactory    A factory that returns {@link Transaction} instances of type {@code T}.
     * @param postRepositoryFactory A factory that creates a new {@link PostRepository} from a provided {@link T}.
     * @param userRepositoryFactory A factory that creates a new {@link UserRepository} from a provided {@link T}.
     */
    public PostFacade(
            Supplier<T> transactionFactory,
            Function<T, PostRepository> postRepositoryFactory,
            Function<T, UserRepository> userRepositoryFactory,
            Function<T, ImagePostImageRepository> imagePostImageRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.postRepositoryFactory = postRepositoryFactory;
        this.userRepositoryFactory = userRepositoryFactory;
        this.imagePostImageRepositoryFactory = imagePostImageRepositoryFactory;
    }

    /**
     * Creates a new post using the provided information.
     *
     * @param user The user making the post.
     * @param body The body of the post.
     * @return The newly created post instance.
     * @throws ResourceNotFoundException When a user with the provided id does not exist.
     */
    public TextPost createTextPost(AuthenticationContext user, String body) throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            PostRepository pr = postRepositoryFactory.apply(transaction);

            TextPost post = pr.createTextPost(user.getUser(), body, LocalDateTime.now());
            transaction.commit();
            return post;
        }
    }

    public ImagePost createImagePost(AuthenticationContext auth, String body, List<String> images) throws ResourceNotFoundException, UnsupportedImageFormatException, ImageThumbnailerException
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            PostRepository           pr               = postRepositoryFactory.apply(transaction);
            ImagePostImageRepository ir               = imagePostImageRepositoryFactory.apply(transaction);
            List<ImagePostImage>     postImages       = new ArrayList<>();
            ImageThumbnailer         imageThumbnailer = new ImageThumbnailer(250, 250);
            DataUriEncoder           uriEncoder       = new DataUriEncoder();
            User                     user             = auth.getUser();

            for (String image : images) {
                byte[] data = Base64.getDecoder().decode(image);
                data = imageThumbnailer.createThumbnail(data);
                String uri = uriEncoder.encode(data, ImageType.fromData(data));
                postImages.add(ir.create(image, user, uri));
            }

            ImagePost post = pr.createImagePost(user, body, LocalDateTime.now(), postImages);
            transaction.commit();
            return post;
        }
    }

    /**
     * Returns the post with the provided id.
     *
     * @param id The id of the post to return.
     * @return The post with the provided id.
     * @throws ResourceNotFoundException When a post with the provided id does not exist.
     */
    public Post get(Integer id) throws ResourceNotFoundException
    {
        try (PostRepository pr = postRepositoryFactory.apply(transactionFactory.get())) {
            Post post = pr.get(id);
            if (post == null)
                throw new ResourceNotFoundException(Post.class, id);

            return post;
        }
    }

    /**
     * Returns a rolling paginated view of the timeline of the user with the provided id.
     * <p>
     * This method returns up to {@code pageSize} results after the provided {@code cutoff}, meaning that
     * only posts older than the {@code cutoff} are retrieved.
     *
     * @param user     The user to return the timeline of.
     * @param pageSize The maximum number of results per request.
     * @param cutoff   The id of cutoff post. Meaning that the id of the first returned post is {@code cutoff + 1}.
     * @return The paginated view of the timeline of the user with the provided id.
     */
    public List<Post> getTimelinePosts(Integer user, Integer pageSize, Integer cutoff)
    {
        try (PostRepository pr = postRepositoryFactory.apply(transactionFactory.get())) {
            return pr.getTimelinePosts(user, pageSize, cutoff);
        }
    }

    /**
     * Returns all the posts of the user with the provided id.
     *
     * @param id The id of the user to return the posts of.
     * @return The posts of the user with the provided id.
     * @throws ResourceNotFoundException When a user with the provided id does not exist.
     */
    public List<Post> getPostByUser(Integer id) throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            UserRepository ur   = userRepositoryFactory.apply(transaction);
            PostRepository pr   = postRepositoryFactory.apply(transaction);
            User           user = ur.get(id);
            if (user == null)
                throw new ResourceNotFoundException(Post.class, user.getId());

            return pr.getByUser(user);
        }
    }

    /**
     * Returns a rolling paginated view of the posts created by the user with the provided id.
     * <p>
     * This method returns up to {@code pageSize} results after the provided {@code cutoff}, meaning that
     * only posts older than the {@code cutoff} are retrieved.
     *
     * @param userId   The user to return the posts of.
     * @param pageSize The maximum number of results per request.
     * @param cutoff   The id of cutoff post. Meaning that the id of the first returned post is {@code cutoff + 1}.
     * @return The paginated view of the posts created by the user with the provided id.
     */
    public List<Post> getRollingPostByUser(Integer userId, Integer pageSize, Integer cutoff) throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            UserRepository ur   = userRepositoryFactory.apply(transaction);
            PostRepository pr   = postRepositoryFactory.apply(transaction);
            User           user = ur.get(userId);
            if (user == null)
                throw new ResourceNotFoundException(Post.class, user.getId());

            return pr.getRollingPosts(user, pageSize, cutoff);
        }
    }
}

