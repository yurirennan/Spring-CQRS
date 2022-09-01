package com.yuri.simplescqrs.demo.command.service;

import com.yuri.simplescqrs.demo.command.entities.User;
import com.yuri.simplescqrs.demo.command.enums.DatabaseOperations;
import com.yuri.simplescqrs.demo.command.exceptions.DeleteUserException;
import com.yuri.simplescqrs.demo.command.exceptions.UpdateUserException;
import com.yuri.simplescqrs.demo.command.repository.ControleTableRepository;
import com.yuri.simplescqrs.demo.command.repository.UserCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCommandService {
    private final UserCommandRepository commandRepository;
    private final ControleTableRepository controleTableRepository;

    @Autowired
    public UserCommandService(UserCommandRepository commandRepository, ControleTableRepository controleTableRepository) {
        this.commandRepository = commandRepository;
        this.controleTableRepository = controleTableRepository;
    }

    public void save(String name, String lastName) {
        Long userId = this.commandRepository.save(name, lastName);

        this.controleTableRepository.updateControleTable(User.TABLE_NAME, userId, DatabaseOperations.CREATE.toString(), false);
    }

    public void update(Long id, String name, String lastName) {
        Long userId = this.commandRepository.update(id, name, lastName);

        if (userId == null) {
            throw new UpdateUserException("Execeção de update");
        }

        this.controleTableRepository.updateControleTable(User.TABLE_NAME, userId, DatabaseOperations.UPDATE.toString(), false);
    }

    public void delete(Long id) {
        Long userId = this.commandRepository.delete(id);

        if (userId == null) {
            throw new DeleteUserException("Execeção de delete");
        }

        this.controleTableRepository.updateControleTable(User.TABLE_NAME, userId, DatabaseOperations.DELETE.toString(), false);
    }
}
