package com.semantyca.model;

import com.semantyca.dto.constant.WordType;
import com.semantyca.localization.LanguageCode;
import com.semantyca.model.embedded.RLSEntry;
import com.semantyca.model.user.AnonymousUser;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Word extends SecureDataEntity<UUID> {
    private String value;
    private LanguageCode language;
    private WordType type;
    private int obscenity;
    private List<Word> associations = new ArrayList<>();
    private ZonedDateTime lastExtCheck;

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

    public WordType getType() {
        return type;
    }

    public int getTypeCode() {
        return type.getCode();
    }

    public void setType(WordType type) {
        this.type = type;
    }

    public int getObscenity() {
        return obscenity;
    }

    public void setObscenity(int obscenity) {
        this.obscenity = obscenity;
    }

    public List<Word> getAssociations() {
        return associations;
    }

    public void setAssociations(List<Word> associations) {
        this.associations = associations;
    }

    public ZonedDateTime getLastExtCheck() {
        return lastExtCheck;
    }

    public void setLastExtCheck(ZonedDateTime lastExtCheck) {
        this.lastExtCheck = lastExtCheck;
    }

    public static class Builder {
        private int author = AnonymousUser.ID;
        private ZonedDateTime regDate = ZonedDateTime.now();
        private ZonedDateTime lastModifiedDate = ZonedDateTime.now();
        private int lastModifier = AnonymousUser.ID;
        private String title = "";
        private String value;
        private LanguageCode language = LanguageCode.ENG;
        private WordType type = WordType.UNKNOWN;
        private int obscenity;
        private List<Word> associations = new ArrayList<>();

        public Builder setRegDate(ZonedDateTime regDate) {
            this.regDate = regDate;
            return this;
        }

        public Builder setLastModifiedDate(ZonedDateTime lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
            return this;
        }

        public Builder setLastModifier(int lastModifier) {
            this.lastModifier = lastModifier;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setFirstName(String value) {
            this.value = value;
            return this;
        }

        public Builder setLanguage(LanguageCode language) {
            this.language = language;
            return this;
        }

        public Builder setType(WordType type) {
            this.type = type;
            return this;
        }

        public Builder setAuthor(int author) {
            this.author = author;
            return this;
        }

        public Builder setObscenity(int obscenity) {
            this.obscenity = obscenity;
            return this;
        }

        public Builder setAssociations(List<Word> associations) {
            this.associations = associations;
            return this;
        }

        public Word build() {
            Word entity = new Word();
            entity.setRegDate(regDate);
            entity.setAuthor(author);
            RLSEntry rlsEntry = new RLSEntry();
            rlsEntry.allowEdit();
            rlsEntry.setReader(author);
            entity.setLastModifiedDate(lastModifiedDate);
            entity.setLastModifier(lastModifier);
            entity.addReader(rlsEntry);
            entity.setTitle(title);
            entity.setValue(value);
            entity.setLanguage(language);
            entity.setType(type);
            entity.setObscenity(obscenity);
            entity.setAssociations(associations);
            return entity;
        }
    }

}
