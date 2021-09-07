package com.semantyca.model;

import com.semantyca.localization.LanguageCode;

import java.util.UUID;

public class Verb extends DataEntity<UUID> {
    private String value;
    private LanguageCode language;
    private int emphasisRank;
    private int formalRank;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LanguageCode getLanguage() {
        return language;
    }

    public void setLanguage(LanguageCode language) {
        this.language = language;
    }

    public int getEmphasisRank() {
        return emphasisRank;
    }

    public void setEmphasisRank(int emphasisRank) {
        this.emphasisRank = emphasisRank;
    }

    public int getFormalRank() {
        return formalRank;
    }

    public void setFormalRank(int formalRank) {
        this.formalRank = formalRank;
    }
}
