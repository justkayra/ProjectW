package com.semantyca.projectw.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.semantyca.projectw.dto.constant.OutcomeType;
import com.semantyca.projectw.dto.constant.PayloadType;
import com.semantyca.projectw.dto.constant.ResultType;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractOutcome<T extends AbstractOutcome> implements IOutcome {
    protected OutcomeType type = OutcomeType.UNKNOWN;
    protected String title;
    protected  Map<String, Object> payloads = new LinkedHashMap();

    public AbstractOutcome setTitle(String title) {
        this.title = title;
        return this;
    }


    public AbstractOutcome setType(OutcomeType type) {
        this.type = type;
        return this;
    }

    @JsonIgnore
    public T addPayload(String payloadName, Object payload){
        payloads.put(payloadName, payload);
        return (T) this;
    }

    @JsonIgnore
    public T addPayload(Object payload){
        payloads.put(payload.getClass().getSimpleName().toLowerCase(), payload);
        return (T) this;
    }

    @JsonIgnore
    public T addPayload(PayloadType payloadType, Object payload){
        payloads.put(payloadType.getAlias(), payload);
        return (T) this;
    }

    public Map<String, Object> getPayloads() {
        return payloads;
    }

    public String getTitle() {
        return title;
    }

    public OutcomeType getType() {
        return type;
    }

    public String toString() {
        return "type=" + type + ", title=" + title;
    }

    public AbstractOutcome setResult(OutcomeType type, ResultType result) {
        this.type = type;
        title = result.name();
        return this;
    }



}
