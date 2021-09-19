package com.semantyca.projectw.dto.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.semantyca.projectw.dto.AbstractOutcome;
import com.semantyca.projectw.dto.IDTO;
import com.semantyca.projectw.dto.constant.OutcomeType;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentOutcome extends AbstractOutcome<DocumentOutcome> {

    public void addPayload(IDTO dto) {
        type = OutcomeType.DOCUMENT;
        super.addPayload(dto);
    }
}
