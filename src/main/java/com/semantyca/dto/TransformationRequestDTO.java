package com.semantyca.dto;

import com.semantyca.dto.constant.EmphasisType;

public class TransformationRequestDTO {
    private String sourceText;
    private EmphasisType emphasis;


    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public void setEmphasis(EmphasisType emphasis) {
        this.emphasis = emphasis;
    }

    public EmphasisType getEmphasis() {
        return emphasis;
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
            request.setEmphasis(emphasis);
            return request;
        }
    }



}
