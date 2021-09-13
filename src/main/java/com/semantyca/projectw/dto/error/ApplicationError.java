package com.semantyca.projectw.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"entityType","timestamp","message"})
public class ApplicationError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;


    public ApplicationError(String message) {
        timestamp = LocalDateTime.now();
        this.message = message;
    }

    public ApplicationError() {
        
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }


    public void add(String objectName, String field, String code) {
    }
}
