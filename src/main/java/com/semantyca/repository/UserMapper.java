package com.semantyca.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.semantyca.model.user.User;
import org.jdbi.v3.core.statement.StatementContext;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper extends AbstractMapper<User> {
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public User map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
        User entity = new User();
        transferIdInteger(entity, rs);
        transferCommonData(entity, rs);
        entity.setEmail(rs.getString("email"));
        entity.setLogin(rs.getString("login"));
        entity.setPwd(rs.getString("pwd"));
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            CollectionType arrayType = typeFactory.constructCollectionType(List.class, String.class);
            PGobject po = rs.getObject("roles", PGobject.class);
            entity.setRoles(mapper.readValue(po.getValue(), arrayType));
        } catch (Exception e) {
            entity.setRoles(new ArrayList<>());
        }
       return entity;
    }
}
