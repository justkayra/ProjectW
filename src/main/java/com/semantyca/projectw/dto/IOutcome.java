package com.semantyca.projectw.dto;

import com.semantyca.projectw.dto.constant.OutcomeType;
import com.semantyca.projectw.dto.constant.PayloadType;

import java.util.Map;

public interface IOutcome<T> {

    String getIdentifier();

    OutcomeType getType();

    String getTitle();

    Map<String, Object> getPayloads();

    T addPayload(PayloadType exception, Object anything);

}
