package com.hust.grp1.dto.user;

public class UserTagDto {

    private String tag;
    private long postCount;

    public UserTagDto(){

    }

    public UserTagDto(String tag, long postCount) {
        this.tag = tag;
        this.postCount = postCount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getPostCount() {
        return postCount;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }
}
