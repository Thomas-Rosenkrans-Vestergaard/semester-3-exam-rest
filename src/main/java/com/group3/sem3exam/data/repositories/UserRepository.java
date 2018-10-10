package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.User;

public interface UserRepository extends CrudRepository<User, Integer>
{

    User createUser(String name, String email, String password);
}
