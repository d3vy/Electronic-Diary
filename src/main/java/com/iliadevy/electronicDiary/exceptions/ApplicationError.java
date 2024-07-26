package com.iliadevy.electronicDiary.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.Date;

@Data
public class ApplicationError  {
    private int status;
    private String message;
    private Date timestamp;

    public ApplicationError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
