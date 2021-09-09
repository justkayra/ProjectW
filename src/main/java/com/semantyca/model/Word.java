package com.semantyca.model;

import com.semantyca.dto.constant.WordType;
import com.semantyca.localization.LanguageCode;
import com.semantyca.model.embedded.RLSEntry;
import com.semantyca.model.user.AnonymousUser;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Word extends SecureDataEntity<UUID> {
    private String value;
    private LanguageCode language;
    private WordType type;

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

    public void setType(WordType type) {
        this.type = type;
    }

    public static class Builder {
        private int author = AnonymousUser.ID;
        private ZonedDateTime regDate = ZonedDateTime.now();
        private ZonedDateTime lastModifiedDate = ZonedDateTime.now();
        private int lastModifier = AnonymousUser.ID;
        private String title = "";
        private String value;
        private LanguageCode language = LanguageCode.ENG;
        private WordType type;

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
            return entity;
        }
    }

}
