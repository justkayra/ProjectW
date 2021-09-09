package com.semantyca.repository;

import com.semantyca.model.Word;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WordRepository implements IRepository<Word> {


    public static Uni<Word> findById(PgPool dbClient, int i, int i1) {
        return null;
    }

    public static Multi<Word> findAllUnrestricted (PgPool client, final int limit, final int offset) {
        return client.query("SELECT * FROM words").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(WordRepository::from);

    }

    public static Uni<UUID> save(PgPool client, Word word) {
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

        return  client
                .preparedQuery("INSERT INTO words (reg_date, title, author, last_mod_date, last_mod_user, value, language, type)" +
                        "VALUES('" + Date.from(word.getRegDate().toInstant()) + "',$1,$2,'" + Date.from(word.getLastModifiedDate().toInstant()) + "',$3,$4,$5,$6) RETURNING id")
                .execute(tuple)
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().get(UUID.class,"id"));
    }

    private static Word from(Row row) {
        return new Word();
    }


}
