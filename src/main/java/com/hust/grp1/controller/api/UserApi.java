package com.hust.grp1.controller.api;

import com.hust.grp1.dto.ApiResponseDto;
import com.hust.grp1.dto.post.PostedItemByPageDto;
import com.hust.grp1.dto.user.*;
import com.hust.grp1.exception.InvalidPasswordException;
import com.hust.grp1.exception.PasswordNotMatchException;
import com.hust.grp1.exception.UserNotFoundException;
import com.hust.grp1.repository.CustomRepository;
import com.hust.grp1.service.PostedItemService;
import com.hust.grp1.service.UserService;
import com.hust.grp1.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.hust.grp1.dto.ApiResponseDto.StatusCode.*;

@RestController
@RequestMapping("/api/user")
public class UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private PostedItemService postedItemService;

    private Logger LOG = LogManager.getLogger(UserApi.class);

    @GetMapping("/{id}")
    public ApiResponseDto getUserInfo(@PathVariable(value = "id") int id) {

        try {
            UserInfoDto userInfoDto = userService.getUserInfo(id);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, userInfoDto);
        } catch (UserNotFoundException e) {
            return new ApiResponseDto(ApiResponseDto.StatusCode.USER_NOT_FOUND);
        }
    }

    @GetMapping("")
    public ApiResponseDto getUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(required = false) String searchKey,
                                   @RequestParam(name = "sortType", defaultValue = "SORT_BY_REPUTATION") String sortTypeStr) {

        CustomRepository.GetUsersSortType sortType;
        switch (sortTypeStr) {
            case "SORT_BY_NEWEST":
                sortType = CustomRepository.GetUsersSortType.SORT_BY_NEWEST;
                break;
            case "SORT_BY_QUESTION":
                sortType = CustomRepository.GetUsersSortType.SORT_BY_QUESTION;
                break;
            default:
                sortType = CustomRepository.GetUsersSortType.SORT_BY_REPUTATION;
        }

        UserPageDto userInfoDtos = userService.getUsersInfo(searchKey, page, sortType);
        return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, userInfoDtos);
    }

    @PostMapping("/auth/edit")
    public ApiResponseDto editUser(Authentication authentication, @RequestBody UserCreateDto dto){
        if(authentication == null) return new ApiResponseDto(ACCESS_DENIED);
        try{
            userService.editUser(dto);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS);
        } catch (UserNotFoundException e){
            return new ApiResponseDto(ApiResponseDto.StatusCode.USER_NOT_FOUND);
        }
    }

    @GetMapping("/auth/bookmarks")
    public ApiResponseDto getBookmarks(Authentication authentication, @RequestParam(required = true) int page) {
        try {
            if(authentication == null) throw new IllegalAccessException();
            String username = authentication.getName();
            PostedItemByPageDto bookmarkedItems = postedItemService.getBookmarks(username, page);
            return new ApiResponseDto(SUCCESS, bookmarkedItems);
        } catch (IllegalAccessException e) {
            LOG.debug("User has not login: ", e);
            return new ApiResponseDto(ApiResponseDto.StatusCode.ACCESS_DENIED);
        } catch (Exception e) {
            LOG.debug("Unknown exception: ", e);
            return new ApiResponseDto(UNKNOWN_ERROR);
        }
    }

    @GetMapping("/auth/questions")
    public ApiResponseDto getQuestions(Authentication authentication, @RequestParam(required = true) int page){
        try{
            if(authentication == null) throw new IllegalAccessException();
            String username = authentication.getName();
            return new ApiResponseDto(SUCCESS, postedItemService.getQuestions(username, page));
        } catch (IllegalAccessException e) {
            LOG.debug("User has not login: ", e);
            return new ApiResponseDto(ACCESS_DENIED);
        } catch (Exception e){
            LOG.debug("Unknown exception: ", e);
            return new ApiResponseDto(UNKNOWN_ERROR);
        }
    }

    @GetMapping("/auth/comments")
    public ApiResponseDto getComments(Authentication authentication,@RequestParam(required = true) int page) {
        try {
            if (authentication == null) throw new IllegalAccessException();
            String username = authentication.getName();
            return new ApiResponseDto(SUCCESS, postedItemService.getComments(username, page));
        } catch (IllegalAccessException e) {
            LOG.debug("User has not login: ", e);
            return new ApiResponseDto(ACCESS_DENIED);
        } catch (Exception e) {
            LOG.debug("Unknown exception: ", e);
            return new ApiResponseDto(UNKNOWN_ERROR);
        }
    }

    @GetMapping("/auth/tags")
    public ApiResponseDto getUserTags(Authentication authentication) {

        String username = authentication.getName();

        try {
            List<UserTagDto> userTags = userService.getAllTags(username);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, userTags);
        } catch (Exception e) {
            LOG.debug("Unknown error: ", e);
            return new ApiResponseDto(ApiResponseDto.StatusCode.UNKNOWN_ERROR);
        }
    }

    @PostMapping("/auth/change-password")
    public ApiResponseDto changePassword(Authentication authentication, @RequestBody ChangePasswordDto dto) {
        String username = authentication.getName();
        try {
            if(!StringUtil.validatePassword(dto.getNewPassword())) throw new InvalidPasswordException();
            userService.changePassword(username, dto);
            return new ApiResponseDto(SUCCESS);
        } catch (UserNotFoundException e) {
            LOG.debug("User not found ", e);
            return new ApiResponseDto(USER_NOT_FOUND);
        } catch (PasswordNotMatchException e) {
            LOG.debug("Password not match ", e);
            return new ApiResponseDto(PASSWORD_NOT_MATCH);
        } catch (InvalidPasswordException e) {
            LOG.debug("Invalid password ", e);
            return new ApiResponseDto(INVALID_PASSWORD);
        } catch (Exception e) {
            LOG.debug("Unknown exception: ", e);
            return new ApiResponseDto(UNKNOWN_ERROR);
        }
    }
}
