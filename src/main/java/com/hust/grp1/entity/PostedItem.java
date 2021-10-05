package com.hust.grp1.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posted_item")
public class PostedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition="TEXT")
    private String title;
    @Column(columnDefinition="TEXT")
    private String content;
    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedDate;
    @JoinColumn(name = "solution_id")
    @OneToOne(cascade = CascadeType.ALL)
    private PostedItem solution;
    @Column(columnDefinition="TEXT")
    private String code;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PostedItem parent;

    private int upvoteCount;
    private int commentCount;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostedItem> children;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemTag> tags;

    @OneToMany(mappedBy = "postedItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Upvote> upvotes;

    @Transient
    private int currentUserVote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

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

    public List<ItemTag> getTags() {
        return tags;
    }

    public void setTags(List<ItemTag> tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public PostedItem getSolution() {
        return solution;
    }

    public void setSolution(PostedItem solution) {
        this.solution = solution;
    }

    public PostedItem getParent() {
        return parent;
    }

    public void setParent(PostedItem parent) {
        this.parent = parent;
    }

    public List<PostedItem> getChildren() {
        return children;
    }

    public void setChildren(List<PostedItem> children) {
        this.children = children;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getCurrentUserVote() {
        return currentUserVote;
    }

    public void setCurrentUserVote(int currentUserVote) {
        this.currentUserVote = currentUserVote;
    }

    @PreRemove
    public void perRemove(){
        if (parent != null) {
            if (parent.getSolution() == this)
                parent.setSolution(null);
            parent.decreaseCommentCount();
        }

        owner.decreaseReputation(upvoteCount);
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public void onVote(int vote) {
        this.upvoteCount += vote;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void increaseCommentCount() {
        this.commentCount += 1;
    }

    public void decreaseCommentCount() {
        this.commentCount -= 1;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public List<Upvote> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(List<Upvote> upvotes) {
        this.upvotes = upvotes;
    }
}
