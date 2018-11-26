package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.ImagePostImage;
import com.group3.sem3exam.data.entities.User;

public interface ImagePostImageRepository
{
    ImagePostImage create(String data, User user, String thumbnail);
}
