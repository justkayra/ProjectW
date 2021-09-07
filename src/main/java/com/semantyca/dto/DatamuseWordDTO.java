package com.semantyca.dto;

import com.semantyca.dto.constant.WordType;

import java.util.List;

public class DatamuseWordDTO {
    private String word;
    private long score;
    private WordType type;
    private List<String> tags;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public WordType getType() {
        return type;
    }

    public void setType(WordType type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String toString() {
        return word + " " + score;
    }
}
