package com.hust.grp1.dto;

public class GetPostsByTagDto {
    private String tag;
    private int page;
    private int size;
    private String sortTypeStr;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortTypeStr() {
        return sortTypeStr;
    }

    public void setSortTypeStr(String sortTypeStr) {
        this.sortTypeStr = sortTypeStr;
    }
}
