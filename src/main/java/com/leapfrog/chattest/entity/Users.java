package com.leapfrog.chattest.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;
    private String displayName;
    private String password;
    private LocalDateTime createdAt = LocalDateTime.now();

    private Boolean isOnline = false;
}
