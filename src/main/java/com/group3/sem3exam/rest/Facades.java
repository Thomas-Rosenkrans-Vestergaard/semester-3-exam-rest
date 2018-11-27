package com.group3.sem3exam.rest;

import com.group3.sem3exam.data.repositories.*;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import com.group3.sem3exam.data.services.JpaServiceRepository;
import com.group3.sem3exam.logic.*;
import com.group3.sem3exam.logic.authentication.jwt.JpaJwtSecret;
import com.group3.sem3exam.logic.images.ImageFacade;

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
            transaction -> new JpaImagePostImageRepository(transaction)
    );

    public static final RegionFacade region = new RegionFacade(() -> new JpaRegionRepository(JpaConnection.create()));

    static {
        try {
            authentication = new AuthenticationFacade(
                    new JpaJwtSecret(JpaConnection.create().createEntityManager(), 512 / 8),
                    () -> new JpaUserRepository(JpaConnection.create()),
                    () -> new JpaServiceRepository(JpaConnection.create()));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
