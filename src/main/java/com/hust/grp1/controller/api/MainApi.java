package com.hust.grp1.controller.api;


import com.hust.grp1.dto.ApiResponseDto;
import com.hust.grp1.dto.user.UserCreateDto;
import com.hust.grp1.entity.User;
import com.hust.grp1.exception.InvalidPasswordException;
import com.hust.grp1.exception.UserExistedException;
import com.hust.grp1.exception.UserNotFoundException;
import com.hust.grp1.service.UserService;
import com.hust.grp1.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.hust.grp1.dto.ApiResponseDto.StatusCode.*;

@RestController
@RequestMapping(path = "/api")
public class MainApi {

    @Autowired
    private UserService userService;

    private Logger LOG = LogManager.getLogger(MainApi.class);

    @PostMapping("/register")
    public ApiResponseDto register(@RequestBody UserCreateDto newUser){
        try {
            if(!StringUtil.validatePassword(newUser.getPlainTextPassword())) throw new InvalidPasswordException();
            userService.createUser(newUser);
            return new ApiResponseDto(SUCCESS);
        } catch (UserExistedException e) {
            LOG.debug("Username existed: ", e);
            return new ApiResponseDto(USER_EXISTED);
        } catch (InvalidPasswordException e) {
            LOG.debug("Invalid password ", e);
            return new ApiResponseDto(INVALID_PASSWORD);
        }
    }

    @GetMapping("/auth/info")
    public ApiResponseDto authInfo(Authentication authentication) {
        if(authentication == null) return new ApiResponseDto(ACCESS_DENIED);
        try {
            User user = userService.getUserFullInfo(authentication.getName());
            return new ApiResponseDto(SUCCESS, user);
        } catch (UserNotFoundException e) {
            return new ApiResponseDto(USER_NOT_FOUND);
        }
    }

}
