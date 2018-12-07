package com.group3.sem3exam.logic;


import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.ImageRepository;
import com.group3.sem3exam.data.repositories.PostRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.images.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
    private final Function<T, UserRepository> userRepositoryFactory;

    /**
     * A factory that creates a new {@link ImageRepository} from a provided {@link T}.
     */
    private final Function<T, ImageRepository> imageRepositoryFactory;

    /**
     * Creates a new {@link PostFacade}.
     *
     * @param transactionFactory     A factory that returns {@link Transaction} instances of type {@code T}.
     * @param postRepositoryFactory  A factory that creates a new {@link PostRepository} from a provided {@link T}.
     * @param userRepositoryFactory  A factory that creates a new {@link UserRepository} from a provided {@link T}.
     * @param imageRepositoryFactory A factory that creates a new {@link ImageRepository} from a provided {@link T}.
     */
    public PostFacade(
            Supplier<T> transactionFactory,
            Function<T, PostRepository> postRepositoryFactory,
            Function<T, UserRepository> userRepositoryFactory,
            Function<T, ImageRepository> imageRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.postRepositoryFactory = postRepositoryFactory;
        this.userRepositoryFactory = userRepositoryFactory;
        this.imageRepositoryFactory = imageRepositoryFactory;
    }

    public interface ImageDeclaration
    {
        String getData();

        String getDescription();
    }

    public Post createPost(AuthenticationContext auth, String body, List<? extends ImageDeclaration> images)
    throws UnsupportedImageFormatException, ImageThumbnailerException
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            PostRepository  pr         = postRepositoryFactory.apply(transaction);
            ImageRepository ir         = imageRepositoryFactory.apply(transaction);
            List<Image>     postImages = new ArrayList<>();
            User            user       = auth.getUser();

            for (ImageDeclaration image : images)
                postImages.add(createPostImage(ir, image, user));

            Post post = pr.create(user, body, postImages, LocalDateTime.now());
            transaction.commit();
            return post;
        }
    }

    public Post delete(AuthenticationContext auth, Integer id) throws ResourceNotFoundException
    {
        try (PostRepository pr = postRepositoryFactory.apply(transactionFactory.get())) {
            pr.begin();
            User user = auth.getUser();
            if (pr.get(id) != null && user.getId() == pr.get(id).getAuthor().getId()) {
                Post post = pr.delete(id);
                pr.commit();
                return post;
            }
        }
        return null;
    }

    private Image createPostImage(ImageRepository ir, ImageDeclaration image, User user)
    throws ImageThumbnailerException, UnsupportedImageFormatException
    {
        ImageThumbnailer imageThumbnailer = new ImageThumbnailer(250, 250);
        DataUriEncoder   uriEncoder       = new DataUriEncoder();

        if (isURL(image.getData()))
            return ir.create(image.getDescription(), image.getData(), image.getData(), user);

        byte[] full      = Base64.getDecoder().decode(image.getData());
        byte[] thumbnail = imageThumbnailer.createThumbnail(full, ImageType.fromData(full));
        return ir.create(
                image.getDescription(),
                uriEncoder.encode(full),
                uriEncoder.encode(thumbnail),
                user);
    }

    public boolean isURL(String test)
    {
        try {
            new URL(test).toURI();
            return true;
        } catch (Exception e) {
            return false;
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
     * @throws ResourceNotFoundException When a user with the provided user id does not exist.
     */
    public List<Post> getTimelinePosts(Integer user, Integer pageSize, Integer cutoff)
    throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            UserRepository userRepository = userRepositoryFactory.apply(transaction);
            PostRepository postRepository = postRepositoryFactory.apply(transaction);

            User fetchedUser = userRepository.get(user);
            if (fetchedUser == null)
                throw new ResourceNotFoundException(User.class, user);

            return postRepository.getTimeline(fetchedUser, pageSize, cutoff);
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

            return pr.getByAuthor(user);
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
    public List<Post> getRollingPostByUser(Integer userId, Integer pageSize, Integer cutoff)
    throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            UserRepository ur   = userRepositoryFactory.apply(transaction);
            PostRepository pr   = postRepositoryFactory.apply(transaction);
            User           user = ur.get(userId);
            if (user == null)
                throw new ResourceNotFoundException(Post.class, user.getId());

            return pr.getByAuthorRolling(user, pageSize, cutoff);
        }
    }

    private Boolean isUrl(String data)
    {
        String regex = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        try {
            return Pattern.compile(regex).matcher(data).matches();
        } catch (PatternSyntaxException exception) {
            return false;
        }
    }
}

