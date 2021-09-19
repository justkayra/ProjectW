package com.semantyca.projectw.repository;

import com.semantyca.projectw.localization.LanguageCode;
import com.semantyca.projectw.model.Word;
import com.semantyca.projectw.dto.constant.WordType;
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
        entity.setLastExtCheck(getDateTime(rs.getTimestamp("last_ext_check")));
        if (includeAssociatedWords) {
            List<Word> associated =  wordRepository.findRelatedWords(entity.getId());
            entity.setAssociations(associated);
        }
        return entity;
    }
}
