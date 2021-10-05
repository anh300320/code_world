package com.hust.grp1.dto.post;

import java.util.ArrayList;
import java.util.List;

public class PostAndRelatedPostDto {

    private PostedItemDto mainPost;
    private List<PostedItemDto> relatedPosts;
    private boolean bookmark;

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public PostAndRelatedPostDto(){
        relatedPosts = new ArrayList<>();
    }

    public PostedItemDto getMainPost() {
        return mainPost;
    }

    public void setMainPost(PostedItemDto mainPost) {
        this.mainPost = mainPost;
    }

    public List<PostedItemDto> getRelatedPosts() {
        return relatedPosts;
    }

    public void setRelatedPosts(List<PostedItemDto> relatedPosts) {
        this.relatedPosts = relatedPosts;
    }
}
