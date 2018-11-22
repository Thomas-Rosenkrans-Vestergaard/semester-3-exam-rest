package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.repositories.CommentRepository;
import com.group3.sem3exam.data.repositories.PostRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

import java.util.function.Function;
import java.util.function.Supplier;

public class CommentFacade<T extends Transaction>
{

    private final Supplier<Transaction>          transactionFactory;
    private final Function<T, CommentRepository> commentRepositoryFactory;
    private final Function<T, PostRepository>    postRepositoryFactory;

    public CommentFacade(Supplier<Transaction> transactionFactory, Function<T, CommentRepository> commentRepositoryFactory, Function<T, PostRepository> postRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.commentRepositoryFactory = commentRepositoryFactory;
        this.postRepositoryFactory = postRepositoryFactory;
    }
}
