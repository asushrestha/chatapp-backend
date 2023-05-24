package com.leapfrog.chattest.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Users participantOne;
    @OneToOne
    private Users participantTwo;

    private LocalDateTime lastMessageDateTime;
    private String lastTextMessage;
}
