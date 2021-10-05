package com.hust.grp1.dto.post;

import java.util.List;

public class PostedItemByPageDto {
    private int currentPage;
    private long totalItems;
    private long totalPages;
    private List<PostedItemDto> posts;

    public PostedItemByPageDto(int currentPage, long totalItems, long totalPages, List<PostedItemDto> posts) {
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.posts = posts;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<PostedItemDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostedItemDto> posts) {
        this.posts = posts;
    }
}
