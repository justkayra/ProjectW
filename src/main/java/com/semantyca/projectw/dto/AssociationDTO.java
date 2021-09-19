package com.semantyca.projectw.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AssociationDTO {
    private String id;
    private String value;
    private int emphasisRank;

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getEmphasisRank() {
        return emphasisRank;
    }

    public void setEmphasisRank(int emphasisRank) {
        this.emphasisRank = emphasisRank;
    }
}
