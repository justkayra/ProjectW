package com.semantyca.projectw.dto;

import com.semantyca.projectw.dto.constant.ReplacementResultType;

public class LegendEntryDTO {
    private String newWord;
    private String oldWord;
    private int emphasisRateOfNewWord;
    private int emphasisRateOfOldWord;
    private ReplacementResultType result;

    public String getNewWord() {
        return newWord;
    }

    public void setNewWord(String newWord) {
        this.newWord = newWord;
    }

    public String getOldWord() {
        return oldWord;
    }

    public void setOldWord(String oldWord) {
        this.oldWord = oldWord;
    }

    public int getEmphasisRateOfNewWord() {
        return emphasisRateOfNewWord;
    }

    public void setEmphasisRateOfNewWord(int emphasisRateOfNewWord) {
        this.emphasisRateOfNewWord = emphasisRateOfNewWord;
    }

    public int getEmphasisRateOfOldWord() {
        return emphasisRateOfOldWord;
    }

    public void setEmphasisRateOfOldWord(int emphasisRateOfOldWord) {
        this.emphasisRateOfOldWord = emphasisRateOfOldWord;
    }

    public ReplacementResultType getResult() {
        return result;
    }

    public void setResult(ReplacementResultType result) {
        this.result = result;
    }

    public static class Builder {
        private String newWord;
        private String oldWord;
        private int emphasisRateOfNewWord;
        private int emphasisRateOfOldWord;
        private ReplacementResultType result = ReplacementResultType.REPLACED;

        public Builder setNewWord(String newWord) {
            this.newWord = newWord;
            return this;
        }

        public Builder setOldWord(String oldWord) {
            this.oldWord = oldWord;
            return this;
        }

        public Builder setEmphasisRateOfNewWord(int emphasisRateOfNewWord) {
            this.emphasisRateOfNewWord = emphasisRateOfNewWord;
            return this;
        }

        public Builder setEmphasisRateOfOldWord(int emphasisRateOfOldWord) {
            this.emphasisRateOfOldWord = emphasisRateOfOldWord;
            return this;
        }

        public Builder setResult(ReplacementResultType result) {
            this.result = result;
            return this;
        }

        public LegendEntryDTO build() {
            LegendEntryDTO dto = new LegendEntryDTO();
            dto.setOldWord(oldWord);
            dto.setNewWord(newWord);
            dto.setEmphasisRateOfOldWord(emphasisRateOfOldWord);
            dto.setEmphasisRateOfNewWord(emphasisRateOfNewWord);
            dto.setResult(result);
            return dto;
        }
    }

}
