package com.leapfrog.chattest.repository;

import com.leapfrog.chattest.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    @Query("SELECT messages FROM ChatMessage messages WHERE messages.chatRoom.id=:chatRoomId order by messages.sentDateTime asc")
    List<ChatMessage> getChatMessages(Long chatRoomId);
}
