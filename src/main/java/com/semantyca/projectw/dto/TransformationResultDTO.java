package com.semantyca.projectw.dto;

import com.semantyca.projectw.dto.delta.Entry;

import java.util.List;

public class TransformationResultDTO {
    private String type;
    private List<Entry> children;
    private List<LegendEntryDTO> legendEntries;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Entry> getChildren() {
        return children;
    }

    public void setChildren(List<Entry> children) {
        this.children = children;
    }

    public List<LegendEntryDTO> getLegendEntries() {
        return legendEntries;
    }

    public void setLegendEntries(List<LegendEntryDTO> legendEntries) {
        this.legendEntries = legendEntries;
    }
}
