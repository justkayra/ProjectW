package com.semantyca.projectw.dto;

import com.semantyca.projectw.dto.constant.EmphasisType;

public class TransformationRequestDTO {
    private String sourceText;
    private EmphasisType emphasisType;


    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public void setEmphasisType(EmphasisType emphasisType) {
        this.emphasisType = emphasisType;
    }

    public EmphasisType getEmphasisType() {
        return emphasisType;
    }

    public static class Builder {
        private String sourceText;
        private EmphasisType emphasis = EmphasisType.RANDOM;

        public Builder setSourceText(String sourceText) {
            this.sourceText = sourceText;
            return this;
        }

        public Builder setEmphasis(EmphasisType emphasis) {
            this.emphasis = emphasis;
            return this;
        }

        public TransformationRequestDTO build() {
            TransformationRequestDTO request = new TransformationRequestDTO();
            request.setSourceText(sourceText);
            request.setEmphasisType(emphasis);
            return request;
        }
    }



}
