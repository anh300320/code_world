package com.hust.grp1.dto.user;

import java.util.List;

public class UserPageDto {
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private List<UserInfoDto> users;

    public UserPageDto(int currentPage, long totalItems, int totalPages, List<UserInfoDto> users) {
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.users = users;
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

    public List<UserInfoDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoDto> users) {
        this.users = users;
    }
}
