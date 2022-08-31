package com.yuri.simplescqrs.demo.command.resource;

import com.yuri.simplescqrs.demo.command.entities.User;
import com.yuri.simplescqrs.demo.command.service.UserCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserCommandResource {

    private final UserCommandService commandService;

    @Autowired
    public UserCommandResource(UserCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody User user) {
        this.commandService.save(user.getName(), user.getLastName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody User user, @PathVariable("id") Long id) {
        this.commandService.update(id, user.getName(), user.getLastName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        this.commandService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

