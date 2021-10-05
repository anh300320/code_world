package com.hust.grp1.entity.id;

import java.io.Serializable;
import java.util.Objects;

public class BookmarkId implements Serializable {

    private Integer postedItem;
    private Integer user;

    public Integer getPostedItem() {
        return postedItem;
    }

    public void setPostedItem(Integer postedItem) {
        this.postedItem = postedItem;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkId that = (BookmarkId) o;
        return postedItem.equals(that.postedItem) &&
                user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postedItem, user);
    }
}
