package com.hust.grp1.dto.post;

import com.hust.grp1.dto.tag.TagDto;

import java.util.List;

public class TagByPageDto {
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private List<TagDto> tags;

    public TagByPageDto(int currentPage, long totalItems, int totalPages, List<TagDto> posts) {
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.tags = posts;
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }
}
