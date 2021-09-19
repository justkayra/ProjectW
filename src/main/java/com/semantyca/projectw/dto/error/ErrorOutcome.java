package com.semantyca.projectw.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.semantyca.projectw.dto.AbstractOutcome;
import com.semantyca.projectw.dto.constant.OutcomeType;
import com.semantyca.projectw.repository.exception.DocumentExists;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"identifier", "type", "title", "pageName", "payloads"})
public class ErrorOutcome extends AbstractOutcome<ErrorOutcome> {

    public ErrorOutcome(DocumentExists e, OutcomeType type) {
        super();
        this.type = type;
        title = e.getMessage();
    }


}
