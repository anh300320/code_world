package com.hust.grp1.dto.post;

public class UpvoteResponeDto {

    private int vote;

    public UpvoteResponeDto(){}

    public UpvoteResponeDto(int vote){
        this.vote = vote;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
