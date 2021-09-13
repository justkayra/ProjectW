package com.semantyca.projectw.repository;

import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;
import java.util.UUID;

public class UUIDArgumentFactory extends AbstractArgumentFactory<UUID> {

    public UUIDArgumentFactory() {
        super(Types.JAVA_OBJECT);
    }

    @Override
    protected Argument build(UUID id, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, id.toString());
    }
}
