package com.leapfrog.chattest.entity;

import lombok.Data;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Service
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String plainText;

    private LocalDateTime sentDateTime;

    @OneToOne
    private ChatRoom chatRoom;

    @OneToOne
    private Users sentBy;
}
