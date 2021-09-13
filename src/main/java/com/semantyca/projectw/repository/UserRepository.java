package com.semantyca.projectw.repository;

import com.semantyca.projectw.model.user.User;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserRepository {

    private Jdbi jdbi;

    public UserRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Optional<User> findById(int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, reg_date,title, author, last_mod_date, last_mod_user, login, pwd, email, name, roles " +
                        "FROM users WHERE id = '" + userId + "'")
                        .map(new UserMapper()).findFirst());
    }

    public Optional<User> findByLogin(String login) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, reg_date,title, author, last_mod_date, last_mod_user, login, pwd, email, name, roles " +
                        "FROM users WHERE login = '" + login + "'")
                        .map(new UserMapper()).findFirst());

    }

    public List<User> findAllUnrestricted(int limit, int offset) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM users LIMIT " + limit + " OFFSET " + offset)
                        .map(new UserMapper()).list());
    }

    public void bareInsert(User user) {
        jdbi.useHandle(handle -> {
            handle.registerArgument(new RolesArgumentFactory());
            handle.createUpdate("INSERT INTO users (reg_date, title, author, last_mod_date, last_mod_user, login, pwd, name, email, roles)" +
                    " VALUES (:regDate, :title, :author, :lastModifiedDate, :lastModifier, :login, :pwd, :name, :email, :roles::jsonb)")
                    .bindBean(user)
                    .execute();
        });
    }

    public void bareDelete(User user) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM users WHERE id = :id")
                    .bind("id", user.getId())
                    .execute();
        });
    }


}
