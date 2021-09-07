package com.semantyca.model;

import com.semantyca.localization.LanguageCode;
import com.semantyca.model.embedded.RLSEntry;
import com.semantyca.model.user.AnonymousUser;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Adjective extends SecureDataEntity<UUID> {
    private String value;
    private LanguageCode language;
    private int emphasisRank;
    private int formalRank;
    private List<Adjective> associations = new ArrayList<>();

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

    public int getEmphasisRank() {
        return emphasisRank;
    }

    public void setEmphasisRank(int emphasisRank) {
        this.emphasisRank = emphasisRank;
    }

    public int getFormalRank() {
        return formalRank;
    }

    public void setFormalRank(int formalRank) {
        this.formalRank = formalRank;
    }

    public void setAssociations(List<Adjective> associations) {
        this.associations = associations;
    }

    public List<Adjective> getAssociations() {
        return associations;
    }

    public void addAssociation(Adjective adjective) {
        associations.add(adjective);
    }

    public static class Builder {
        private int author = AnonymousUser.ID;
        private String authorName = AnonymousUser.USER_NAME;
        private ZonedDateTime regDate = ZonedDateTime.now();
        private ZonedDateTime lastModifiedDate = ZonedDateTime.now();
        private int lastModifier = AnonymousUser.ID;
        private String lastModifierName = AnonymousUser.USER_NAME;
        private String title = "";
        private String value;
        private LanguageCode language = LanguageCode.ENG;
        private int emphasisRank;
        private int formalRank;
        private List<Adjective> associations = new ArrayList<>();

        public Builder setAuthorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

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

        public Builder setLastModifierName(String lastModifierName) {
            this.lastModifierName = lastModifierName;
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

        public Builder setEmphasisRank(int emphasisRank) {
            this.emphasisRank = emphasisRank;
            return this;
        }

        public Builder setFormalRank(int formalRank) {
            this.formalRank = formalRank;
            return this;
        }

        public Builder setAssociations(List<Adjective> associations) {
            this.associations = associations;
            return this;
        }

        public Adjective build() {
            Adjective adjective = new Adjective();
            adjective.setRegDate(regDate);
            adjective.setAuthor(author);
            RLSEntry rlsEntry = new RLSEntry();
            rlsEntry.setReader(author);
            adjective.addReader(rlsEntry);
            adjective.setValue(value);
            adjective.setLanguage(language);
            adjective.setEmphasisRank(emphasisRank);
            adjective.setFormalRank(formalRank);
            adjective.setLastModifiedDate(lastModifiedDate);
            adjective.setLastModifier(lastModifier);
            adjective.setAssociations(associations);
            return adjective;
        }
    }

}
