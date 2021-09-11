package com.semantyca.dto.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.semantyca.dto.AbstractOutcome;
import com.semantyca.dto.constant.OutcomeType;
import com.semantyca.model.DataEntity;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentOutcome extends AbstractOutcome<DocumentOutcome> {

    public DocumentOutcome setPayload(DataEntity entity) {
        type = OutcomeType.DOCUMENT;
        payloads.put(entity.getEntityType(), entity);
        return this;
    }
}
