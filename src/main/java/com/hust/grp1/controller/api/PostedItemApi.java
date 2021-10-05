package com.hust.grp1.controller.api;

import com.hust.grp1.config.ApplicationConstants;
import com.hust.grp1.dto.ApiResponseDto;

import com.hust.grp1.dto.post.*;
import com.hust.grp1.exception.*;
import com.hust.grp1.repository.CustomRepository;
import com.hust.grp1.service.PostedItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hust.grp1.dto.ApiResponseDto.StatusCode.*;

@RestController
@RequestMapping(path = "/api/post")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostedItemApi {

    @Autowired
    private PostedItemService postedItemService;

    private Logger LOG = LogManager.getLogger(PostedItemApi.class);

    @GetMapping("/{id}")
    public ApiResponseDto getPostedItem(Authentication authentication, @PathVariable("id") Integer id){

        try {
            String username = null;
            if(authentication != null) username = authentication.getName();
            PostAndRelatedPostDto dto = postedItemService.getPostedItem(id, username);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, dto);
        } catch (ItemDoesNotExistException e) {
            LOG.debug("Item does not exist: ", e);
            return new ApiResponseDto(ITEM_NOT_FOUND);
        }
    }

    /**
     * Create post and comment to post
     */
    @PostMapping("/auth/create")
    public ApiResponseDto createPostedItem(Authentication authentication, @RequestBody PostedItemDto postedItem){
        String ownerUsername = authentication.getName();
        try {
            PostedItemDto postedItemDto = postedItemService.createPostedItem(postedItem, ownerUsername);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, postedItemDto);
        } catch (UserNotFoundException e) {
            LOG.debug("User not found: ", e);
            return new ApiResponseDto(ApiResponseDto.StatusCode.USER_NOT_FOUND);
        } catch (ItemDoesNotExistException e) {
            LOG.debug("Item not exist: ", e);
            return new ApiResponseDto(ITEM_NOT_FOUND);
        } catch (CommentMustNotHasTagsException e) {
            LOG.debug("Cannot create a comment with tags: ", e);
            return new ApiResponseDto(COMMENT_TAGS_NOT_ALLOWED);
        }
    }

    @PostMapping("/auth/upvote")
    public ApiResponseDto upvote(Authentication authentication, @RequestBody UpvoteDto dto){

        try {
            if(dto.getVote() != 1 && dto.getVote() != -1) throw new IllegalArgumentException();
            UpvoteResponeDto upvoteResponeDto = postedItemService.upvote(authentication.getName() ,dto);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, upvoteResponeDto);
        } catch (ItemDoesNotExistException e) {
            LOG.debug("Item does not exist: ", e);
            return new ApiResponseDto(ITEM_NOT_FOUND);
        } catch (UserNotFoundException e) {
            LOG.debug("User not found: ", e);
            return new ApiResponseDto(ApiResponseDto.StatusCode.USER_NOT_FOUND);
        } catch (IllegalArgumentException e){
            LOG.debug("Bad request: ", e);
            return new ApiResponseDto(ApiResponseDto.StatusCode.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ApiResponseDto getPostedItem(@RequestParam(required = false) String searchKey,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "15") int size){
        Pageable pageable = PageRequest.of(page, size);
        if(searchKey == null || searchKey.equals("")){
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, postedItemService.getPostedItem(pageable));
        } else {
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS, postedItemService.getPostedItem(pageable, searchKey));
        }
    }

    @GetMapping("/auth/delete/{id}")
    public ApiResponseDto deletePostedItem(Authentication authentication, @PathVariable(value = "id") int itemId) {

        String username = authentication.getName();
        try {
            if(!authentication.getAuthorities().contains(ApplicationConstants.AUTHORITY_ADMIN)
                    && !postedItemService.isItemOwner(itemId, username))
                throw new IllegalAccessException();

            postedItemService.deletePostedItem(itemId, username);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS);
        } catch (ItemDoesNotExistException e) {
            LOG.debug("Item does not exist ", e);
            return new ApiResponseDto(ITEM_NOT_FOUND);
        } catch (IllegalAccessException e) {
            LOG.debug("Invalid access");
            return new ApiResponseDto(ApiResponseDto.StatusCode.ACCESS_DENIED);
        }
    }

    @GetMapping("/auth/bookmark/{id}")
    public ApiResponseDto bookmarkItem(Authentication authentication, @PathVariable(value = "id") int itemId) {
        try {
            if(authentication == null) throw new IllegalAccessException();
            String username = authentication.getName();
            postedItemService.bookmarkItem(username, itemId);
            return new ApiResponseDto(ApiResponseDto.StatusCode.SUCCESS);
        } catch (UserNotFoundException e) {
            return new ApiResponseDto(ApiResponseDto.StatusCode.USER_NOT_FOUND);
        } catch (IllegalAccessException e) {
            LOG.debug("User has not login: ", e);
            return new ApiResponseDto(ApiResponseDto.StatusCode.ACCESS_DENIED);
        }
    }

    @GetMapping("/auth/solve/{id}")
    public ApiResponseDto solvePostedItem(Authentication authentication, @PathVariable(value = "id") int itemId) {

        String username = authentication.getName();
        try {
            postedItemService.solve(username, itemId);
            return new ApiResponseDto(SUCCESS);
        } catch (ItemDoesNotExistException e) {
            return new ApiResponseDto(ITEM_NOT_FOUND);
        } catch (NotPostOwnerException e) {
            return new ApiResponseDto(NOT_POSTED_ITEM_OWNER);
        } catch (SolvingParentException e) {
            return new ApiResponseDto(SOLVING_PARENT_ITEM);
        } catch (Exception e) {
            LOG.debug("Unknown error: ", e);
            return new ApiResponseDto(UNKNOWN_ERROR);
        }
    }

    @GetMapping("")
    public ApiResponseDto getPostedItems(@RequestParam(defaultValue = "NEWEST") String sortTypeStr,
                                         @RequestParam(required = true) int page,
                                         @RequestParam(defaultValue = "15") int size,
                                         @RequestParam(required = false) String searchKey) {

        CustomRepository.GetPostedItemSortType sortType;

        switch (sortTypeStr) {
            case "UPVOTE":
                sortType = CustomRepository.GetPostedItemSortType.SORT_BY_UPVOTE;
                break;
            case "COMMENT":
                sortType = CustomRepository.GetPostedItemSortType.SORT_BY_COMMENT;
                break;
            default:
                sortType = CustomRepository.GetPostedItemSortType.SORT_BY_NEWEST;
        }

        try {
            QuestionsPageDto questionsPageDto = postedItemService.getPostedItems(page, size, sortType, searchKey);
            return new ApiResponseDto(SUCCESS, questionsPageDto);
        } catch (Exception e) {
            LOG.debug("Unknown exception: ", e);
            return new ApiResponseDto(UNKNOWN_ERROR);
        }
    }

    @PostMapping("/auth/update")
    public ApiResponseDto updatePostedItem(Authentication authentication, @RequestBody UpdatePostedItemDto dto) {
        try {
            if(authentication == null) throw new IllegalAccessException();
            String username = authentication.getName();
            postedItemService.updatePostedItem(username, dto);
            return new ApiResponseDto(SUCCESS);
        } catch (ItemDoesNotExistException e) {
            LOG.debug("Posted itemn does not exist: ", e);
            return new ApiResponseDto(ITEM_NOT_FOUND);
        } catch (IllegalAccessException e) {
            LOG.debug("Invalid access: ", e);
            return new ApiResponseDto(ACCESS_DENIED);
        }
    }

    @GetMapping("/hot")
    public ApiResponseDto getHotQuestions() {

        try {
            List<QuestionDto> hotQuestions = postedItemService.getHotQuestions();
            return new ApiResponseDto(SUCCESS, hotQuestions);
        } catch (Exception e) {
            LOG.debug("Unknown exception ", e);
            return new ApiResponseDto(UNKNOWN_ERROR);
        }
    }
}
