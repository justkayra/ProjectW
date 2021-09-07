package com.semantyca.dto.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.semantyca.dto.AbstractOutcome;
import com.semantyca.dto.constant.OutcomeType;
import com.semantyca.repository.exception.DocumentExists;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"identifier", "type", "title", "pageName", "payloads"})
public class ErrorOutcome extends AbstractOutcome<ErrorOutcome> {

    public ErrorOutcome(DocumentExists e, OutcomeType type) {
        super();
        identifier = e.getClass().getSimpleName().toLowerCase() + "_exception";
        this.type = type;
        title = e.getMessage();
    }

    @JsonIgnore
    public String getPageName() {
        return pageName;
    }


}
