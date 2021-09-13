package com.semantyca.projectw.dto.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.semantyca.projectw.dto.AbstractOutcome;
import com.semantyca.projectw.model.DataEntity;
import com.semantyca.projectw.dto.constant.OutcomeType;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentOutcome extends AbstractOutcome<DocumentOutcome> {

    public DocumentOutcome setPayload(DataEntity entity) {
        type = OutcomeType.DOCUMENT;
        payloads.put(entity.getEntityType(), entity);
        return this;
    }
}
