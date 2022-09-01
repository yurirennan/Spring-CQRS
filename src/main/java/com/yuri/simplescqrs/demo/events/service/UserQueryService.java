package com.yuri.simplescqrs.demo.events.service;

import com.yuri.simplescqrs.demo.events.entities.User;
import com.yuri.simplescqrs.demo.events.exceptions.UserNotFoundException;
import com.yuri.simplescqrs.demo.scheduler.repository.UserQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryService {
    private final UserQueryRepository repository;

    @Autowired
    public UserQueryService(UserQueryRepository repository) {
        this.repository = repository;
    }

    public User getById(Long id) {
        Optional<User> userOptional = this.repository.findByUserId(id);

        if(!userOptional.isPresent()) {
            throw new UserNotFoundException("Usuário não encontrado!");
        }

        User user = userOptional.get();
        return user;
    }
}
