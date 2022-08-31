package com.yuri.simplescqrs.demo.command.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.HashMap;
import java.util.Map;

public class ControleTableRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(ControleTableRepository.class);
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Autowired
    public ControleTableRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    public void updateControleTable(String tableName, Long idRecurso, String tipoAlteracao, Boolean sync) {
        final String sql = "INSERT INTO t_controle(table_name, id_recurso, tipo_alteracao, sincronizado) VALUES(:tableName, :idRecurso, :tipoAlteracao, :sincronizado)";

        LOGGER.info("Inserindo em t_controle");

        final Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("idRecurso", idRecurso);
        params.put("tipoAlteracao", tipoAlteracao);
        params.put("sincronizado", sync);

        this.namedParameterJdbcOperations.update(sql, params);

        LOGGER.info("Inserindo com sucesso!");
    }
}
