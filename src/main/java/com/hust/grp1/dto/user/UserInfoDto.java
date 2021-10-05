package com.hust.grp1.dto.user;

import java.time.LocalDateTime;

public class UserInfoDto {

    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private long reputation;
    private String about;
    private long questionCount;
    private long commentCount;
    private long bookmarkCount;
    private LocalDateTime createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getReputation() {
        return reputation;
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public long getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(long questionCount) {
        this.questionCount = questionCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public long getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(long bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}

