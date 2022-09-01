package com.yuri.simplescqrs.demo.command.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserCommandRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public static final Logger LOGGER = LoggerFactory.getLogger(UserCommandRepository.class);

    @Autowired
    public UserCommandRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    public Long save(String name, String lastName) {
        final String sql = "INSERT INTO t_users(name, last_name, data_criacao) VALUES(:name, :lastName, now()) RETURNING id";

        LOGGER.info("Inserindo usuario");

        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("lastName", lastName);

        final Long id = this.namedParameterJdbcOperations.query(sql, params, resultSet -> {
            resultSet.next();
            long resultId = resultSet.getLong("id");

            return resultId;
        });

        LOGGER.info("Inserindo com sucesso!");

        return id;
    }


    public Long update(Long id, String name, String lastName) {
        final String sql = "UPDATE t_users SET name = :name, last_name = :lastName WHERE data_remocao IS NULL AND id = :id RETURNING id";

        LOGGER.info("Atualizando usuario");

        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("lastName", lastName);

        final ResultSetExtractor<Long> extractor = resultSet -> {

            if (resultSet.next()) {
                long resultId = resultSet.getLong("id");

                return resultId;
            }

            return null;
        };


        final Long userId = this.namedParameterJdbcOperations.query(sql, params, extractor);

        LOGGER.info("Atualizado com sucesso!");

        return userId;
    }

    public Long delete(Long id) {
        final String sql = "UPDATE t_users SET data_remocao = now() where data_remocao IS NULL AND id = :id RETURNING id";

        LOGGER.info("Atualizando usuario");

        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        final ResultSetExtractor<Long> extractor = resultSet -> {

            if (resultSet.next()) {
                long resultId = resultSet.getLong("id");

                return resultId;
            }

            return null;
        };

        final Long userId = this.namedParameterJdbcOperations.query(sql, params, extractor);

        LOGGER.info("Atualizado com sucesso!");

        return userId;
    }
}
