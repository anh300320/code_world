package com.hust.grp1.entity;

import com.hust.grp1.entity.id.BookmarkId;

import javax.persistence.*;

@Entity
@Table(name = "book_mark")
@IdClass(BookmarkId.class)
public class Bookmark {

    @Id
    @ManyToOne
    private PostedItem postedItem;
    @Id
    @ManyToOne
    private User user;

    private boolean notify;

    public Bookmark(){
    }

    public Bookmark(int itemId, int userId) {
        postedItem = new PostedItem();
        postedItem.setId(itemId);
        user = new User();
        user.setId(userId);
    }

    public PostedItem getPostedItem() {
        return postedItem;
    }

    public void setPostedItem(PostedItem postedItem) {
        this.postedItem = postedItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
