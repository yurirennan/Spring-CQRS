package com.yuri.simplescqrs.demo.scheduler.repository;

import com.yuri.simplescqrs.demo.events.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserQueryRepository extends MongoRepository<User, Long> {
    @Query("{userId : ?0}")
    Optional<User> findByUserId(Long id);

    @Query(value="{userId : ?0}", delete = true)
    void deleteByUserId(Long id);

    @Query(value = "{userId : ?0}")
    Optional<User> updateByUserId(Long id);
}
