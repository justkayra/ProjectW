package com.semantyca.dto;

public class SlateTextElementDTO {
    private String text;
    private boolean bold;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text.replace("&#32;", " ").replace("&#44;",",").replace("&#46;",".");
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }


}
