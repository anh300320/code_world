package com.hust.grp1.dto.post;

import com.hust.grp1.entity.Upvote;

public class UpvoteDto {

    private int itemId;
    /**
     * vote must be 1 or -1
     * */
    private int vote;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        if(vote >= 1) this.vote = 1;
        else if(vote <= -1) this.vote = -1;
        else this.vote = 0;
    }
}
