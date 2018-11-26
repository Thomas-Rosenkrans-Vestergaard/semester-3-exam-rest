package com.group3.sem3exam.logic;


import com.group3.sem3exam.data.entities.*;
import com.group3.sem3exam.data.repositories.ImagePostImageRepository;
import com.group3.sem3exam.data.repositories.PostRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.images.*;
import jdk.internal.util.xml.impl.Input;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
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
     * @param title  The description of the post.
     * @param body   the body of the post.
     * @param author The id of the user who wrote the post (author).
     * @return The newly created post instance.
     * @throws ResourceNotFoundException When a user with the provided id does not exist.
     */
    public TextPost createTextPost(String title, String body, Integer author) throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            PostRepository pr   = postRepositoryFactory.apply(transaction);
            UserRepository ur   = userRepositoryFactory.apply(transaction);
            User           user = ur.get(author);
            if (user == null)
                throw new ResourceNotFoundException(User.class, author);
            TextPost post = pr.createTextPost(user, title, body, LocalDateTime.now());
            transaction.commit();
            return post;
        }
    }

    public ImagePost createImagePost(String title, String body, Integer author, List<String> images) throws Exception
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            PostRepository           pr               = postRepositoryFactory.apply(transaction);
            UserRepository           ur               = userRepositoryFactory.apply(transaction);
            ImagePostImageRepository ir               = imagePostImageRepositoryFactory.apply(transaction);
            User                     user             = ur.get(author);
            List<ImagePostImage>     postImages       = new ArrayList<>();
            ImageThumbnailer         imageThumbnailer = new ImageThumbnailer(250, 250);
            DataUriEncoder               uriEncoder       = new DataUriEncoder();
            if (user == null)
                throw new ResourceNotFoundException(User.class, author);

            for(String image: images){
                byte[] data = isUrl(image) ? getDataFrom(image) : Base64.getDecoder().decode(image);
                data = imageThumbnailer.createThumbnail(data);
                String uri = uriEncoder.encode(data, ImageType.fromData(data));
                postImages.add(ir.create(image, user, uri));
            }
            ImagePost post = pr.createImagePost(user, title, body, LocalDateTime.now(), postImages);
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

           private Boolean isUrl (String data)
           {
               String regex = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
               try {
                   return Pattern.compile(regex).matcher(data).matches();
               } catch (PatternSyntaxException exception) {
                   return false;
               }
           }

           private byte[] getDataFrom (String image) throws IOException
           {
               URL               url = new URL(image);
               HttpURLConnection con = (HttpURLConnection) url.openConnection();
               con.setRequestMethod("GET");
               int status = con.getResponseCode();
               if (status == 200) {
                   ByteArrayOutputStream out = convertInputStreamToOutputStream(con.getInputStream());
                   return out.toByteArray();
               }
               throw new IOException("det virkede ikke prøv igen fjols");
           }


           private ByteArrayOutputStream convertInputStreamToOutputStream(InputStream in) throws IOException
           {
               byte[] buffer = new byte[1024];
               int len;
               ByteArrayOutputStream out = new ByteArrayOutputStream();
               while ((len = in.read(buffer)) != -1) {
                   out.write(buffer, 0, len);
               }
               return out;
           }
}

