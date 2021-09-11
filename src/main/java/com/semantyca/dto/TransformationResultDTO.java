package com.semantyca.dto;

import java.util.List;

public class TransformationResultDTO {
    private String type;
    private List<SlateTextElementDTO> children;
    private List<LegendEntryDTO> legendEntries;

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

    public List<LegendEntryDTO> getLegendEntries() {
        return legendEntries;
    }

    public void setLegendEntries(List<LegendEntryDTO> legendEntries) {
        this.legendEntries = legendEntries;
    }
}
