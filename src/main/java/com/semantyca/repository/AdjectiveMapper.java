package com.semantyca.repository;

import com.semantyca.localization.LanguageCode;
import com.semantyca.model.Adjective;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Deprecated
public class AdjectiveMapper extends AbstractMapper<Adjective> {

    private boolean includeAssociatedWords;
    AdjectiveRepository adjectiveRepository;

    public AdjectiveMapper(AdjectiveRepository adjectiveRepository, boolean includeAssociatedWords) {
        super();
        this.adjectiveRepository = adjectiveRepository;
        this.includeAssociatedWords = includeAssociatedWords;
    }

    @Override
    public Adjective map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
        Adjective entity = new Adjective();
        transferIdUUID(entity, rs);
        transferCommonData(entity, rs);
        entity.setValue(rs.getString("value"));
        entity.setLanguage(LanguageCode.valueOf(rs.getString("language")));
        entity.setEmphasisRank(rs.getInt("emphasisRank"));
        entity.setFormalRank(rs.getInt("formalRank"));
        if (includeAssociatedWords) {
            List<Adjective> associated =  adjectiveRepository.findAssociatedWord(entity.getId());
            entity.setAssociations(associated);
        }
        return entity;
    }
}
