package com.okta.beyondid.application.model.dto;

public class ApiResponseDto<T> {

    public Integer status;
    public T data;
    public String message;

    public ApiResponseDto(Integer status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
