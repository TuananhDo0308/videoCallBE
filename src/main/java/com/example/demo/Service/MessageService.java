package com.example.demo.Service;

import com.example.demo.DTO.MessageDto;
import com.example.demo.DTO.MessageResponseDto;
import com.example.demo.Model.Message;
import com.example.demo.Model.RoomChat;
import com.example.demo.Model.User;
import com.example.demo.Repository.MessageRepository;
import com.example.demo.Repository.RoomChatRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoomChatRepository roomChatRepository;
    @Autowired
    MessageRepository messageRepository;
    // Gửi tin nhắn

    @Transactional
    public Message sendMessage(MessageDto messageDto, String username) {
        User sender = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RoomChat roomChat = roomChatRepository.findById(messageDto.getRoomChatId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        System.out.println(roomChat.getParticipants().toString());

        Message message = new Message().builder()
                .content(messageDto.getContent())
                .sender(sender)
                .timestamp(LocalDateTime.now())
                .roomChat(roomChat)
                .build();

        roomChat.addMessage(message);
        return messageRepository.save(message);
    }

    // Lấy danh sách tin nhắn trong phòng
    public List<MessageResponseDto> getMessagesByRoomId(Long roomChatId) {
        List<Message> messages = messageRepository.findByRoomChatId(roomChatId);
        return messages.stream().map(message -> {
            MessageResponseDto responseDto = new MessageResponseDto();
            responseDto.setId(message.getId());
            responseDto.setContent(message.getContent());
            responseDto.setSenderUsername(message.getSender().getName());
            responseDto.setRoomChatId(message.getRoomChat().getId());
            responseDto.setTimestamp(message.getTimestamp());
            return responseDto;
        }).collect(Collectors.toList());
    }

}
