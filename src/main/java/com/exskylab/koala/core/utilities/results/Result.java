package com.exskylab.koala.core.utilities.results;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class Result {
    private boolean success;
    private String message;
    private HttpStatus httpStatus;
    private String path;
    private LocalDateTime timeStamp = LocalDateTime.now();
    private String correlationId;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getTimeStamp(){
        return timeStamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Result(boolean success, String message, HttpStatus httpStatus, String path) {
        this.success = success;
        this.message = message;
        this.httpStatus = httpStatus;
        this.path = path;
        this.correlationId = MDC.get("correlationId");
    }

    public Result(boolean success, HttpStatus httpStatus, String path) {
        this.success = success;
        this.httpStatus = httpStatus;
        this.path = path;
        this.correlationId = MDC.get("correlationId");
    }
}
