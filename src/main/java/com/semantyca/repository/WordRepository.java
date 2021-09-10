package com.semantyca.repository;

import com.semantyca.dto.constant.WordType;
import com.semantyca.localization.LanguageCode;
import com.semantyca.model.Word;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WordRepository extends AbstractRepository {

    private PgPool client;

    private AdjectiveRepository adjectiveRepository;

    private Jdbi jdbi;


    @Inject
    public WordRepository(Jdbi jdbi, PgPool client) {
        this.client = client;
        this.jdbi = jdbi;
    }

    public Optional<Word> findByValue(String word) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM words a WHERE a.value = '" + word + "'")
                        .map(new WordMapper()).findFirst());
    }

    public Uni<Word> findByWordAsync(String word) {
        return client.preparedQuery("SELECT * FROM words WHERE value = $1").execute(Tuple.of(word))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Multi<Word> findAllUnrestricted (final int limit, final int offset) {
        return client.query("SELECT * FROM words").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(WordRepository::from);

    }

    public Uni<UUID> save(Word word) {
        List columns = new ArrayList();
        //columns.add(Date.from(word.getRegDate().toInstant()));
        columns.add(word.getTitle());
        columns.add(word.getAuthor());
        //columns.add(Date.from(word.getLastModifiedDate().toInstant()));
        columns.add(word.getLastModifier());
        columns.add(word.getValue());
        columns.add(word.getLanguage());
        columns.add(word.getType().getCode());
        Tuple tuple = Tuple.tuple(columns);

        return  client.preparedQuery("INSERT INTO words (reg_date, title, author, last_mod_date, last_mod_user, value, language, type)" +
                        "VALUES('" + Date.from(word.getRegDate().toInstant()) + "',$1,$2,'" + Date.from(word.getLastModifiedDate().toInstant()) + "',$3,$4,$5,$6) RETURNING id")
                .execute(tuple)
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().get(UUID.class,"id"));
    }

    private static Word from(Row row) {
        Word entity = new Word();
        transferIdUUID(entity, row);
        transferCommonData(entity, row);
        entity.setType(WordType.getType(row.getInteger("type")));
        entity.setValue(row.getString("value"));
        entity.setLanguage(LanguageCode.valueOf(row.getString("language")));
        return entity;
    }


}
