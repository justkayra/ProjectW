package com.semantyca.repository;

import com.semantyca.model.IDataEntity;
import io.vertx.mutiny.sqlclient.Row;

import java.time.ZoneId;

public class AbstractRepository {
    private static final ZoneId zoneId = ZoneId.of( "Europe/Riga" );

    public static void transferIdInteger(IDataEntity entity, Row row)  {
        entity.setId(row.getLong("id"));
    }

    public static void transferIdUUID(IDataEntity entity, Row row){
        entity.setId(row.getUUID("id"));

    }

    public static void transferCommonData(IDataEntity entity, Row row)  {
        entity.setLastModifiedDate(row.getLocalDateTime("last_mod_date").atZone(zoneId));
        entity.setLastModifier(row.getInteger("last_mod_user"));
        entity.setRegDate(row.getLocalDateTime("reg_date").atZone(zoneId));
        entity.setTitle(row.getString("title"));
        entity.setAuthor(row.getInteger("author"));
    }
}
