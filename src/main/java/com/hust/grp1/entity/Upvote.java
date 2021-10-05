package com.hust.grp1.entity;

import com.hust.grp1.entity.id.UpvoteId;

import javax.persistence.*;

@Entity
@Table(name = "upvote", indexes = @Index(columnList = "item_id"))
@IdClass(UpvoteId.class)
public class Upvote {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User upvoteUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private PostedItem item;
    /**
     * vote must be 1 or -1
     * */
    private int vote;

    public Upvote() {
    }

    public Upvote(User upvoteUser, PostedItem postedItem){
        this.upvoteUser = upvoteUser;
        this.item = postedItem;
    }

    public User getUpvoteUser() {
        return upvoteUser;
    }

    public void setUpvoteUser(User user) {
        this.upvoteUser = user;
    }

    public PostedItem getItem() {
        return item;
    }

    public void setItem(PostedItem item) {
        this.item = item;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
