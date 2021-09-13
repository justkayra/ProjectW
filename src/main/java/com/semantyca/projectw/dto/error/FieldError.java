package com.semantyca.projectw.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"error", "helperText"})
public class FieldError{
    boolean isError = true;
    String helperText;

    public FieldError(String fieldName, String errorMessage) {
        this.helperText = errorMessage;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }



    public String getHelperText() {
        return helperText;
    }
}
