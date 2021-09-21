package com.semantyca.projectw.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class AssociationDTO {
    private String id;
    private String value;
    private int emphasisRank;
    private boolean isBaseWord;

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

    public boolean isBaseWord() {
        return isBaseWord;
    }

    public void setBaseWord(boolean baseWord) {
        isBaseWord = baseWord;
    }

    public static class Builder {
        private String id;
        private String value;
        private int emphasisRank;
        private boolean isBaseWord;


        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setId(UUID id) {
            this.id = id.toString();
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setEmphasisRank(int emphasisRank) {
            this.emphasisRank = emphasisRank;
            return this;
        }

        public Builder isBaseWord(boolean baseWord) {
            isBaseWord = baseWord;
            return this;
        }

        public AssociationDTO build() {
            AssociationDTO associationDTO = new AssociationDTO();
            associationDTO.setId(id);
            associationDTO.setValue(value);
            associationDTO.setEmphasisRank(emphasisRank);
            associationDTO.setBaseWord(isBaseWord);
            return associationDTO;
        }


    }

}
