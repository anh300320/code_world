package com.hust.grp1.dto.post;

import com.hust.grp1.dto.user.UserCreateDto;

import java.time.LocalDateTime;
import java.util.List;

public class PostedItemDto {

    private Integer id;
    private String title;
    private String content;
    private UserCreateDto owner;
    private LocalDateTime createdDate;
    private PostedItemDto parent;
    private int upvoteCount;
    private List<PostedItemDto> comments;
    private List<String> tags;
    private String code;
    private int currentUserVote;
    private PostedItemDto solution;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserCreateDto getOwner() {
        return owner;
    }

    public void setOwner(UserCreateDto owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public PostedItemDto getParent() {
        return parent;
    }

    public void setParent(PostedItemDto parent) {
        this.parent = parent;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public List<PostedItemDto> getComments() {
        return comments;
    }

    public void setComments(List<PostedItemDto> comments) {
        this.comments = comments;
    }

    public int getCurrentUserVote() {
        return currentUserVote;
    }

    public void setCurrentUserVote(int currentUserVote) {
        if(currentUserVote != 1 && currentUserVote != -1 && currentUserVote != 0)
            throw new IllegalArgumentException();
        this.currentUserVote = currentUserVote;
    }

    public PostedItemDto getSolution() {
        return solution;
    }

    public void setSolution(PostedItemDto solution) {
        this.solution = solution;
    }
}
