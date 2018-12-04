package com.group3.sem3exam.rest;

import com.group3.sem3exam.data.repositories.*;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.*;
import com.group3.sem3exam.logic.*;
import com.group3.sem3exam.logic.authentication.jwt.JpaJwtSecret;
import com.group3.sem3exam.logic.authentication.jwt.JwtSecret;
import com.group3.sem3exam.logic.chat.ChatFacade;
import com.group3.sem3exam.logic.chat.ChatWebSocketServer;
import com.group3.sem3exam.logic.images.ImageFacade;
import com.group3.sem3exam.logic.services.ServiceFacade;

public class Facades
{

    public static final AuthenticationFacade authentication;
    public static final CityFacade           city    = new CityFacade(() -> new JpaCityRepository(JpaConnection.create()));
    public static final CountryFacade        country = new CountryFacade(() -> new JpaCountryRepository(JpaConnection.create()));

    public static final UserFacade<JpaTransaction>       user       = new UserFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaUserRepository(transaction),
            transaction -> new JpaCityRepository(transaction),
            transaction -> new JpaImageRepository(transaction)
    );
    public static final FriendshipFacade<JpaTransaction> friendship = new FriendshipFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaUserRepository(transaction),
            transaction -> new JpaFriendshipRepository(transaction)
    );

    public static final ImageFacade<JpaTransaction> image = new ImageFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaUserRepository(transaction),
            transaction -> new JpaImageRepository(transaction)
    );

    public static final PostFacade<JpaTransaction> post = new PostFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            transaction -> new JpaPostRepository(transaction),
            transaction -> new JpaUserRepository(transaction),
            transaction -> new JpaImageRepository(transaction)
    );

    public static final CommentFacade comments = new CommentFacade(
            () -> new JpaCommentRepository(JpaConnection.create())
    );

    private static      JwtSecret                     jwtSecret;
    public static final RegionFacade                  region = new RegionFacade(() -> new JpaRegionRepository(JpaConnection.create()));
    public static final ServiceFacade<JpaTransaction> services;

    static {
        try {
            jwtSecret = new JpaJwtSecret(JpaConnection.create().createEntityManager(), 512 / 8);
            authentication = new AuthenticationFacade(
                    jwtSecret,
                    () -> new JpaUserRepository(JpaConnection.create()),
                    () -> new JpaServiceRepository(JpaConnection.create()));

            services = new ServiceFacade<>(
                    () -> new JpaTransaction(JpaConnection.create()),
                    transaction -> new JpaServiceRepository(transaction),
                    transaction -> new JpaAuthRequestRepository(transaction),
                    transaction -> new JpaPermissionRequestRepository(transaction),
                    transaction -> new JpaPermissionTemplateRepository(transaction),
                    transaction -> new JpaUserRepository(transaction),
                    transaction -> new JpaPermissionRepository(transaction),
                    jwtSecret
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static final ChatWebSocketServer<JpaTransaction> chatWebSocketServer;

    public static final ChatFacade<JpaTransaction> chat;

    static {
        chatWebSocketServer = new ChatWebSocketServer<>(
                () -> new JpaTransaction(JpaConnection.create()),
                transaction -> new JpaChatMessageRepository(transaction),
                transaction -> new JpaUserRepository(transaction),
                null
        );

        chat = new ChatFacade<>(chatWebSocketServer,
                                () -> new JpaTransaction(JpaConnection.create()),
                                transaction -> new JpaChatMessageRepository(transaction),
                                transaction -> new JpaUserRepository(transaction),
                                transaction -> new JpaFriendshipRepository(transaction),
                                transaction -> new JpaServiceRepository(transaction),
                                jwtSecret
        );

        chatWebSocketServer.start();
    }
}
