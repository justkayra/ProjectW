package com.semantyca.projectw.dto;

public class SlateTextElementDTO {
    private String text;
    private boolean bold;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text.replace("&#32;", " ")
                .replace("&#44;",",")
                .replace("&#46;",".")
                .replace("&#34;", "\"");
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public static class Builder {
        public SlateTextElementDTO build(String text) {
            SlateTextElementDTO dto = new SlateTextElementDTO();
            dto.setText(text);
            return dto;
        }

        public SlateTextElementDTO build(String text, boolean bold) {
            SlateTextElementDTO dto = new SlateTextElementDTO();
            dto.setBold(bold);
            dto.setText(text);
            return dto;
        }
    }

}
