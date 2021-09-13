package com.semantyca.projectw.repository;

import com.semantyca.projectw.model.Adjective;
import org.jdbi.v3.core.Jdbi;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Deprecated
public class AdjectiveRepository {

    private Jdbi jdbi;

    public AdjectiveRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Optional<Adjective> findById(UUID id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM adjectives a WHERE a.id = '" + id + "'")
                        .map(new AdjectiveMapper(this, true)).findFirst());
    }

    public Optional<Adjective> findByValue(String value) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM adjectives a WHERE a.value = '" + value + "'")
                        .map(new AdjectiveMapper(this, true)).findFirst());
    }

    public List<Adjective> findAssociatedWord(UUID id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM adj_links al, adjectives a WHERE al.primary_adj_id = '" + id + "' and al.related_adj_id = a.id")
                        .map(new AdjectiveMapper(this, false)).list());
    }

    public List<Adjective> findAllUnrestricted(final int limit, final int offset) {
        String sql = "SELECT * FROM adjectives LIMIT " + limit + " OFFSET " + offset;
        if (limit == 0 && offset == 0) {
            sql = "SELECT * FROM adjectives";
        }
        String finalSql = sql;
        return jdbi.withHandle(handle ->
                handle.createQuery(finalSql)
                        .map(new AdjectiveMapper(this, true)).list());
    }

    public Adjective bareInsert(Adjective entity) {
        return jdbi.withHandle(handle -> {
            Adjective adjective =  handle.createUpdate("INSERT INTO adjectives (reg_date, title, author, last_mod_date, last_mod_user, value, language, emphasisRank, formalRank)" +
                    "VALUES (:regDate, :title, :author, :lastModifiedDate, :lastModifier, :value, :language, :emphasisRank, :formalRank )")
                    .bindBean(entity)
                    .executeAndReturnGeneratedKeys()
                    .map(new AdjectiveMapper(this, true))
                    .one();
            return adjective;
        });
    }

    @Transactional
    public Adjective insert(Adjective entity) {
        return jdbi.withHandle(handle -> {
            Adjective adjective = handle.createUpdate("INSERT INTO adjectives (reg_date, title, author, last_mod_date, last_mod_user, value, language, emphasisRank, formalRank)" +
                    "VALUES (:regDate, :title, :author, :lastModifiedDate, :lastModifier, :value, :language, :emphasisRank, :formalRank )")
                    .bindBean(entity)
                    .executeAndReturnGeneratedKeys()
                    .map(new AdjectiveMapper(this , true))
                    .one();
            for (Adjective a : entity.getAssociations()) {
                Optional<Adjective> associatedAdjectiveOptional = findByValue(a.getValue());
                if (associatedAdjectiveOptional.isPresent()) {
                    handle.createUpdate("INSERT INTO adj_links (primary_adj_id, related_adj_id)" +
                            "VALUES (:primary, :related )")
                            .bind("primary", adjective.getId())
                            .bind("related", associatedAdjectiveOptional.get().getId())
                            .execute();
                }
            }
            return adjective;
        });
    }

    @Transactional
    public Adjective update(Adjective entity) {
        return jdbi.withHandle(handle -> {
            Adjective adjective = handle.createUpdate("UPDATE adjectives " +
                    "SET title=:title, last_mod_date=:lastModifiedDate, last_mod_user=:lastModifier, value=:value, emphasisrank=:emphasisRank, formalrank=:formalRank " +
                    "WHERE id=:id")
                    .bindBean(entity)
                    .executeAndReturnGeneratedKeys()
                    .map(new AdjectiveMapper(this, true))
                    .one();
            handle.createUpdate("DELETE FROM adj_links WHERE primary_adj_id=:id")
                    .bind("id", adjective.getId())
                    .execute();
            for (Adjective a : entity.getAssociations()) {
                Optional<Adjective> associatedAdjectiveOptional = findByValue(a.getValue());
                if (associatedAdjectiveOptional.isPresent()) {
                    handle.createUpdate("INSERT INTO adj_links (primary_adj_id, related_adj_id)" +
                            "VALUES (:primary, :related )")
                            .bind("primary", adjective.getId())
                            .bind("related", associatedAdjectiveOptional.get().getId())
                            .execute();
                }
            }
            return adjective;
        });
    }

    public int bareDelete(Adjective adjective) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM adj_links WHERE related_adj_id = :id")
                    .bind("id", adjective.getId())
                    .execute();
            handle.createUpdate("DELETE FROM adj_links WHERE primary_adj_id = :id")
                    .bind("id", adjective.getId())
                    .execute();
            handle.createUpdate("DELETE FROM adjectives WHERE id = :id")
                    .bind("id", adjective.getId())
                    .execute();
        });
        return 0;
    }

}
