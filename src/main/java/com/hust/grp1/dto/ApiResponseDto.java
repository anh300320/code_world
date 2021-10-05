package com.hust.grp1.dto;

public class ApiResponseDto {

    public enum StatusCode {
        SUCCESS,
        UNKNOWN_ERROR,
        USER_NOT_FOUND,
        ACCESS_DENIED,
        ITEM_NOT_FOUND,
        USER_EXISTED,
        BAD_REQUEST,
        NOT_POSTED_ITEM_OWNER,
        SOLVING_PARENT_ITEM,
        COMMENT_TAGS_NOT_ALLOWED,
        PASSWORD_NOT_MATCH,
        INVALID_PASSWORD
    }

    private StatusCode status;
    private Object data;

    public ApiResponseDto(){

    }

    public ApiResponseDto(StatusCode status){
        this.status = status;
    }

    public ApiResponseDto(StatusCode status, Object data) {
        this.status = status;
        this.data = data;
    }

    public StatusCode getStatus() {
        return status;
    }

    public void setStatus(StatusCode status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
