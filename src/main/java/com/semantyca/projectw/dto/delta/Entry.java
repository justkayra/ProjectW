package com.semantyca.projectw.dto.delta;

public class Entry {
    private String insert;
    private Attribute attributes;

    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) {
        this.insert = insert.replace("&#32;", " ")
                .replace("&#44;",",")
                .replace("&#46;",".")
                .replace("&#39;", "'")
                .replace("&#32;", " ")
                .replace("&#34;", "\"");

    }

    public Attribute getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute attributes) {
        this.attributes = attributes;
    }


}
