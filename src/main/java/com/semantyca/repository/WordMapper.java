package com.semantyca.repository;

import com.semantyca.dto.constant.WordType;
import com.semantyca.localization.LanguageCode;
import com.semantyca.model.Word;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WordMapper extends AbstractMapper<Word> {

    private boolean includeAssociatedWords;
    private WordRepository wordRepository;

    public WordMapper(WordRepository adjectiveRepository, boolean includeAssociatedWords) {
        super();
        this.wordRepository = adjectiveRepository;
        this.includeAssociatedWords = includeAssociatedWords;
    }

    @Override
    public Word map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
        Word entity = new Word();
        transferIdUUID(entity, rs);
        transferCommonData(entity, rs);
        entity.setValue(rs.getString("value"));
        entity.setLanguage(LanguageCode.valueOf(rs.getString("language")));
        entity.setType(WordType.getType(rs.getInt("type")));
        entity.setObscenity(rs.getInt("obscenity"));
        if (includeAssociatedWords) {
            List<Word> associated =  wordRepository.findAssociatedWord(entity.getId());
            entity.setAssociations(associated);
        }
        return entity;
    }
}
