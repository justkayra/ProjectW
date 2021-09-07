package com.semantyca.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Types;
import java.util.List;

public class RolesArgumentFactory extends AbstractArgumentFactory<List> {
    private static final Logger LOGGER = LoggerFactory.getLogger("RolesArgumentFactory");
    private static final ObjectMapper mapper = new ObjectMapper();

    public RolesArgumentFactory() {
        super(Types.OTHER);
    }

    @Override
    protected Argument build(List roles, ConfigRegistry config) {
        return (position, statement, ctx) -> {
            try {
                statement.setObject(position, mapper.writeValueAsString(roles), Types.OTHER);
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
            }
        };
    }
}
