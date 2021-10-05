package com.hust.grp1.entity.id;

import java.io.Serializable;
import java.util.Objects;

public class UpvoteId implements Serializable {

    private Integer upvoteUser;
    private Integer item;

    public UpvoteId(){}

    public UpvoteId(Integer upvoteUser, Integer item) {
        this.upvoteUser = upvoteUser;
        this.item = item;
    }

    public Integer getUpvoteUser() {
        return upvoteUser;
    }

    public void setUpvoteUser(Integer user) {
        this.upvoteUser = user;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpvoteId upvoteId = (UpvoteId) o;
        return upvoteUser.equals(upvoteId.upvoteUser) &&
                item.equals(upvoteId.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upvoteUser, item);
    }
}
