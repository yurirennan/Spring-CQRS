package com.yuri.simplescqrs.demo.events.resource;

import com.yuri.simplescqrs.demo.command.service.UserCommandService;
import com.yuri.simplescqrs.demo.events.entities.User;
import com.yuri.simplescqrs.demo.events.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private final UserCommandService commandService;
    private final UserQueryService queryService;

    @Autowired
    public UserResource(UserCommandService commandService, UserQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        User user = this.queryService.getById(id);

        return ResponseEntity.ok(user);
    }
}
