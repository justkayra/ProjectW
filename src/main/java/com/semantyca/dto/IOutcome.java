package com.semantyca.dto;

import com.semantyca.dto.constant.OutcomeType;
import com.semantyca.dto.constant.PayloadType;

import java.util.Map;

public interface IOutcome<T> {

    String getIdentifier();

    OutcomeType getType();

    String getTitle();

    Map<String, Object> getPayloads();

    T addPayload(PayloadType exception, Object anything);

}
