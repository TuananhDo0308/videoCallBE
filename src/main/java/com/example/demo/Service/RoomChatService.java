package com.example.demo.Service;


import com.example.demo.DTO.MessageDto;
import com.example.demo.DTO.MessageResponseDto;
import com.example.demo.DTO.RoomChatDto;
import com.example.demo.Model.Message;
import com.example.demo.Model.RoomChat;
import com.example.demo.Model.User;
import com.example.demo.Repository.MessageRepository;
import com.example.demo.Repository.RoomChatRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Request.JoinRoomRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomChatService {

    @Autowired
    private RoomChatRepository roomChatRepository;

    @Autowired
    private UserRepository userRepository;

    // Tạo mã code ngẫu nhiên cho phòng
    private String generateRoomCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes).substring(0, 8);
    }

    // Tạo phòng mới
    public RoomChat createRoom(RoomChatDto roomChatDto, String email) {
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RoomChat roomChat = new RoomChat();
        roomChat.setName(roomChatDto.getName());
        roomChat.setPassword(roomChatDto.getPassword());
        roomChat.setPrivateRoom(roomChatDto.isPrivate());
        roomChat.setCode(generateRoomCode());
        roomChat.setCreator(creator);

        roomChat.setCreator(creator);
        System.out.println(roomChatDto.isPrivate());
        return roomChatRepository.save(roomChat);
    }

    public List<RoomChat> getAllRooms() {
        return roomChatRepository.findAll();
    }

    // Tìm phòng theo roomId
    public Optional<RoomChat> findByRoomId(Long roomId) {
        return roomChatRepository.findById(roomId);
    }
    // Tham gia phòng bằng mã code
    public RoomChat joinRoom(JoinRoomRequest joinRoomRequest, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RoomChat roomChat = roomChatRepository.findByCode(joinRoomRequest.getCode())
                .orElseThrow(() -> new RuntimeException("Room not found or invalid code"));
        if (roomChat.isPrivateRoom()) {
            if (!roomChat.getPassword().equals(joinRoomRequest.getPassword())) {
                throw new RuntimeException("Incorrect room password");
            }
        }
        // Kiểm tra xem user đã tham gia phòng chưa
        if (roomChat.getParticipants().contains(user)) {
            throw new RuntimeException("User already in the room");
        }

        roomChat.addParticipant(user);
        return roomChatRepository.save(roomChat);
    }

    public List<RoomChat> getJoinedRooms(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + email));

        return List.copyOf(user.getJoinedRooms());
    }

    public List<RoomChat> getCreatedRooms(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return List.copyOf(user.getCreatedRooms());
    }
}

