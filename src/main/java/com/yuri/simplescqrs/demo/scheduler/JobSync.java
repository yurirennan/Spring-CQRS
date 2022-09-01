package com.yuri.simplescqrs.demo.scheduler;

import com.yuri.simplescqrs.demo.events.entities.User;
import com.yuri.simplescqrs.demo.scheduler.dto.ControleDTO;
import com.yuri.simplescqrs.demo.scheduler.repository.UserQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope("singleton")
public class JobSync {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final UserQueryRepository userQueryRepository;
    private final MongoTemplate mongoTemplate;

    public static final Logger LOGGER = LoggerFactory.getLogger(JobSync.class);

    @Autowired
    public JobSync(NamedParameterJdbcOperations namedParameterJdbcOperations, UserQueryRepository userQueryRepository, MongoTemplate mongoTemplate) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.userQueryRepository = userQueryRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleFixedDelayTask() {
        LOGGER.info("Begin Job");
        List<ControleDTO> operationsNotSync = this.getOperationsNotSync();

        this.syncDatabases(operationsNotSync);
    }

    private List<ControleDTO> getOperationsNotSync() {
        final String sql = "SELECT * FROM t_controle WHERE sincronizado = false";

        final Map<String, Object> params = new HashMap<>();

        final ResultSetExtractor<List<ControleDTO>> extractor = resultSet -> {
            final List<ControleDTO> controleDTOS = new ArrayList<>();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long id_recurso = resultSet.getLong("id_recurso");
                String table_name = resultSet.getString("table_name");
                String tipo_alteracao = resultSet.getString("tipo_alteracao");
                boolean sincronizado = resultSet.getBoolean("sincronizado");

                ControleDTO controleDTO = ControleDTO.builder()
                        .id(id)
                        .tableName(table_name)
                        .idRecurso(id_recurso)
                        .tipoAlteracao(tipo_alteracao)
                        .sincronizado(sincronizado)
                        .build();

                controleDTOS.add(controleDTO);
            }

            return controleDTOS;
        };

        List<ControleDTO> controleDTOS = this.namedParameterJdbcOperations.query(sql, params, extractor);

        return controleDTOS;
    }

    private void syncDatabases(List<ControleDTO> controleDTOS) {
        for (ControleDTO controleDTO : controleDTOS) {
            switch (controleDTO.getTipoAlteracao()) {
                case "CREATE":
                    syncCreate(controleDTO);
                    break;
                case "UPDATE":
                    syncUpdate(controleDTO);
                    break;
                case "DELETE":
                    syncDelete(controleDTO);
                    break;
            }
        }
    }


    private void syncCreate(ControleDTO controleDTO) {
        User dataNotSync = getDataNotSync(controleDTO);
        Long idToSync = controleDTO.getId();

        if (dataNotSync != null) {
            LOGGER.info("SYNC CREATE");
            this.userQueryRepository.save(dataNotSync);
            this.updateTableControleSetSyncTrue(idToSync);
        }
    }

    private void syncUpdate(ControleDTO controleDTO) {
        User dataNotSync = getDataNotSync(controleDTO);
        Long idToSync = controleDTO.getId();
        Long idUser = dataNotSync.getUserId();

        if (dataNotSync != null) {
            LOGGER.info("SYNC UPDATE");

            Optional<User> userOptional = this.userQueryRepository.findByUserId(dataNotSync.getUserId());

            Query query = new Query(Criteria.where("userId").is(idUser));
            Update update = new Update();
            User user = userOptional.get();
            user.setName(dataNotSync.getName());
            user.setLastName(dataNotSync.getLastName());
            update.set("name", user.getName());
            update.set("lastName", user.getLastName());

            mongoTemplate.updateFirst(query, update, User.class, "users");
            this.userQueryRepository.updateByUserId(idToSync);

            this.updateTableControleSetSyncTrue(idToSync);
        }
    }

    private void syncDelete(ControleDTO controleDTO) {
        User dataNotSync = getDataNotSync(controleDTO);
        Long idToSync = controleDTO.getId();

        if (dataNotSync != null) {
            LOGGER.info("SYNC DELETE");
            this.userQueryRepository.deleteByUserId(dataNotSync.getUserId());
            this.updateTableControleSetSyncTrue(idToSync);
        }
    }

    private User getDataNotSync(ControleDTO controleDTO) {
        LOGGER.info("Get Data Not Sync");
        String tableName = controleDTO.getTableName();
        Long idRecurso = controleDTO.getIdRecurso();

        final String sql = "SELECT * FROM :tabela WHERE id = :idRecurso"
                .replace(":tabela", tableName);

        final Map<String, Object> params = new HashMap<>();
        params.put("idRecurso", idRecurso);

        User userToSave = this.namedParameterJdbcOperations.query(sql, params, resultSet -> {
            resultSet.next();
            long userId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String lastName = resultSet.getString("last_name");

            User user = User.builder()
                    .userId(userId)
                    .name(name)
                    .lastName(lastName)
                    .build();

            return user;
        });

        return userToSave;
    }

    private void updateTableControleSetSyncTrue(Long idToSync) {

        final String sqlTemplate = "UPDATE t_controle SET sincronizado = true WHERE sincronizado = false AND id = :idToSync";

        final Map<String, Object> params = new HashMap<>();
        params.put("idToSync", idToSync);

        this.namedParameterJdbcOperations.update(sqlTemplate, params);

    }
}
