package com.yuri.simplescqrs.demo.command.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {
    public static final String TABLE_NAME = "t_users";

    @Id
    private Long id;
    private String name;
    private String lastName;
}
