package com.semantyca.dto;

import java.util.List;

public class AdjectiveDTO {
    private String value;
    private List<String>  associations;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getAssociations() {
        return associations;
    }

    public void setAssociations(List<String> associations) {
        this.associations = associations;
    }
}
