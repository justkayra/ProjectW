package com.semantyca.repository;

import com.semantyca.dto.constant.RatingType;
import com.semantyca.model.Word;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WordRepository extends AbstractRepository {

    private Jdbi jdbi;

    @Inject
    public WordRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Optional<Word> findById(UUID id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM words a WHERE a.id = '" + id + "'")
                        .map(new WordMapper(this, true)).findFirst());
    }

    public Optional<Word> findByValue(String word) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM words a WHERE a.value = '" + word + "'")
                        .map(new WordMapper(this, false)).findFirst());
    }

    public List<Word> findAssociatedWord(UUID id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM word_emphasis_rank_links wel, words w WHERE wel.primary_word_id = '" + id + "' and wel.related_word_id = w.id")
                        .map(new WordMapper(this, false)).list());
    }

    public List<Word> findAllUnrestricted(final int limit, final int offset) {
        String sql = "SELECT * FROM words LIMIT " + limit + " OFFSET " + offset;
        if (limit == 0 && offset == 0) {
            sql = "SELECT * FROM words";
        }
        String finalSql = sql;
        return jdbi.withHandle(handle ->
                handle.createQuery(finalSql)
                        .map(new WordMapper(this, true)).list());
    }

    @Transactional
    public Word insert(Word entity) {
        return jdbi.withHandle(handle -> {
            Word word = handle.createUpdate("INSERT INTO words (reg_date, title, author, last_mod_date, last_mod_user, value, language, type, obscenity)" +
                    "VALUES (:regDate, :title, :author, :lastModifiedDate, :lastModifier, :value, :language, :typeCode, :obscenity )")
                    .bindBean(entity)
                    .executeAndReturnGeneratedKeys()
                    .map(new WordMapper(this , true))
                    .one();
            for (Word a : entity.getAssociations()) {
                Optional<Word> associatedOptional = findByValue(a.getValue());
                if (associatedOptional.isPresent()) {
                    handle.createUpdate("INSERT INTO word_emphasis_rank_links (primary_word_id, related_word_id)" +
                            "VALUES (:primary, :related )")
                            .bind("primary", word.getId())
                            .bind("related", associatedOptional.get().getId())
                            .execute();
                    handle.createUpdate("INSERT INTO word_formality_rank_links (primary_word_id, related_word_id)" +
                            "VALUES (:primary, :related )")
                            .bind("primary", word.getId())
                            .bind("related", associatedOptional.get().getId())
                            .execute();
                    //it needs if association has been updated;
                    word = findById(word.getId()).get();
                }
            }
            return word;
        });
    }

    @Transactional
    public int updateRates(Word entity, RatingType ratingType, String associatedWord, int rate) {
        return jdbi.withHandle(handle -> {
            int result = 0;
            if (ratingType == RatingType.EMPHASIS){
                result = handle.createUpdate("UPDATE word_emphasis_rank_links wl SET rank=:emphasisRank WHERE wl.primary_word_id = :id AND related_word_id IN " +
                        "(SELECT id FROM word_emphasis_rank_links wl, words w WHERE w.value = :associatedWord)")
                        .bind("emphasisRank", rate)
                        .bind("id", entity.getId())
                        .bind("associatedWord", associatedWord)
                        .execute();
            }
          return result;
        });
    }

    @Transactional
    public Word update(Word entity) {
        return jdbi.withHandle(handle -> {
            Word word = handle.createUpdate("UPDATE words " +
                    "SET title=:title, last_mod_date=:lastModifiedDate, last_mod_user=:lastModifier, value=:value, type=:typeCode, obscenity=:obscenity " +
                    "WHERE id=:id")
                    .bindBean(entity)
                    .executeAndReturnGeneratedKeys()
                    .map(new WordMapper(this, true))
                    .one();
            handle.createUpdate("DELETE FROM word_emphasis_rank_links WHERE primary_word_id = :id")
                    .bind("id", word.getId())
                    .execute();
            handle.createUpdate("DELETE FROM word_formality_rank_links WHERE primary_word_id = :id")
                    .bind("id", word.getId())
                    .execute();
            for (Word associated : entity.getAssociations()) {
                Optional<Word> optionalWord = findByValue(associated.getValue());
                if (optionalWord.isPresent()) {
                    handle.createUpdate("INSERT INTO word_emphasis_rank_links (primary_word_id, related_word_id)" +
                            "VALUES (:primary, :related )")
                            .bind("primary", word.getId())
                            .bind("related", optionalWord.get().getId())
                            .execute();
                    handle.createUpdate("INSERT INTO word_formality_rank_links (primary_word_id, related_word_id)" +
                            "VALUES (:primary, :related )")
                            .bind("primary", word.getId())
                            .bind("related", optionalWord.get().getId())
                            .execute();
                    //it needs if association has been updated;
                    word = findById(word.getId()).get();
                }
            }
            return word;
        });
    }

    public int bareDelete(Word word) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM word_emphasis_rank_links WHERE related_word_id = :id")
                    .bind("id", word.getId())
                    .execute();
            handle.createUpdate("DELETE FROM word_emphasis_rank_links WHERE primary_word_id = :id")
                    .bind("id", word.getId())
                    .execute();
            handle.createUpdate("DELETE FROM word_formality_rank_links WHERE related_word_id = :id")
                    .bind("id", word.getId())
                    .execute();
            handle.createUpdate("DELETE FROM word_formality_rank_links WHERE primary_word_id = :id")
                    .bind("id", word.getId())
                    .execute();
            handle.createUpdate("DELETE FROM words WHERE id = :id")
                    .bind("id", word.getId())
                    .execute();
        });
        return 0;
    }


}
