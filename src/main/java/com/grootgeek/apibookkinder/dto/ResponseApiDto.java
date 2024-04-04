package com.grootgeek.apibookkinder.dto;


import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseApiDto<T> {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    private boolean success;
    private String code;
    private String dateTime;
    private String message;
    private T data;

    public ResponseApiDto() {
        super();
    }

    public void responseSuccess(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public void responseError(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

}
