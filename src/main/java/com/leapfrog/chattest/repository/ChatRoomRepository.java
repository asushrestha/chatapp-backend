package com.leapfrog.chattest.repository;

import com.leapfrog.chattest.entity.ChatRoom;
import com.leapfrog.chattest.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    @Query(value = "SELECT room FROM ChatRoom room WHERE (room.participantOne=:user OR room.participantTwo=:user) order by room.lastMessageDateTime desc")
    List<ChatRoom> findMyChatRoomList(Users user);

    @Query(value = "SELECT room FROM ChatRoom room WHERE (room.participantOne=:participantOne AND room.participantTwo=:participantTwo) OR (room.participantTwo=:participantOne AND room.participantOne=:participantTwo)order by lastMessageDateTime desc")
    Optional<ChatRoom> findUniqueChatRoomForParticipants(Users participantOne, Users participantTwo);
}
