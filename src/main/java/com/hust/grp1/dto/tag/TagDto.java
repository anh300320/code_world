package com.hust.grp1.dto.tag;

import java.time.LocalDateTime;

public class TagDto {
    private String tag;
    private long numPost;
    private LocalDateTime recentPost;
    private long postToday;

    public TagDto(){
    }

    public TagDto(String tag, long numPost, LocalDateTime recentPost, long postToday) {
        this.tag = tag;
        this.numPost = numPost;
        this.recentPost = recentPost;
        this.postToday = postToday;
    }

    public long getPostToday() {
        return postToday;
    }

    public void setPostToday(long postToday) {
        this.postToday = postToday;
    }

    public LocalDateTime getRecentPost() {
        return recentPost;
    }

    public void setRecentPost(LocalDateTime recentPost) {
        this.recentPost = recentPost;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getNumPost() {
        return numPost;
    }

    public void setNumPost(long numPost) {
        this.numPost = numPost;
    }
}
