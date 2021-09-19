package com.semantyca.projectw.dto;

import java.util.Map;

public class WordDTO implements IDTO {
    private String id;
    private String value;
    private String type;
    private Map<String, AssociationDTO> associations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, AssociationDTO> getAssociations() {
        return associations;
    }

    public void setAssociations(Map<String, AssociationDTO> associations) {
        this.associations = associations;
    }
}
