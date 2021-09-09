package com.semantyca.dto;

import java.util.List;

public class SlateDTO {
    private String type;
    private List<SlateTextElementDTO> children;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SlateTextElementDTO> getChildren() {
        return children;
    }

    public void setChildren(List<SlateTextElementDTO> children) {
        this.children = children;
    }
}
