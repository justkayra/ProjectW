package com.semantyca.repository;

import com.semantyca.dto.constant.WordType;
import com.semantyca.localization.LanguageCode;
import com.semantyca.model.Word;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WordMapper extends AbstractMapper<Word> {

    @Override
    public Word map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
        Word entity = new Word();
        transferIdUUID(entity, rs);
        transferCommonData(entity, rs);
        entity.setValue(rs.getString("value"));
        entity.setLanguage(LanguageCode.valueOf(rs.getString("language")));
        entity.setType(WordType.getType(rs.getInt("type")));

        return entity;
    }
}
