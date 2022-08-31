package com.yuri.simplescqrs.demo.events.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document("users")
@Data
@Builder
public class User {
    @Id
    private Long userId;
    private String name;
    private String lastName;
}