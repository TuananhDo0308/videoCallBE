package com.example.demo.Repository;

import com.example.demo.Model.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomChatRepository extends JpaRepository<RoomChat, Long> {
    Optional<RoomChat> findByCode(String code);
}
