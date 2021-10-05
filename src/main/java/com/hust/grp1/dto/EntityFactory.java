package com.hust.grp1.dto;

import com.hust.grp1.dto.user.UserCreateDto;
import com.hust.grp1.entity.Upvote;
import com.hust.grp1.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EntityFactory {

    public static User createUserEntity(UserCreateDto userCreateDto, PasswordEncoder encoder){

        if(userCreateDto == null || userCreateDto.getUsername() == null || userCreateDto.getPlainTextPassword() == null)
            throw new IllegalArgumentException();

        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(encoder.encode(userCreateDto.getPlainTextPassword()));
        user.setFirstName(userCreateDto.getFirstname());
        user.setLastName(userCreateDto.getLastname());
        return user;
    }

//    public static Upvote createUpvoteEntity(String username, int itemId) {
//
//        Upvote upvote = new Upvote();
//        User user = new User();
//        user.
//    }
}
