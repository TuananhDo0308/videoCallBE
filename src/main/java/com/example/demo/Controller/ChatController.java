package com.example.demo.Controller;

import com.example.demo.DTO.MessageDto;
import com.example.demo.DTO.MessageResponseDto;
import com.example.demo.Model.Message;
import com.example.demo.Model.RoomChat;
import com.example.demo.Service.MessageService;
import com.example.demo.Service.RoomChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;
    // Gửi tin nhắn qua WebSocket
    @MessageMapping("/chat.send/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, @RequestBody MessageDto messageDto, Principal authentication) {
        // Đặt roomChatId vào messageDto
        messageDto.setRoomChatId(roomId);

        // Lấy username từ token
        String username = authentication.getName();
        // Gửi tin nhắn bằng RoomChatService
        Message message = messageService.sendMessage(messageDto, username);

        // Tạo DTO để gửi qua WebSocket
        MessageResponseDto responseDto = new MessageResponseDto();
        responseDto.setId(message.getId());
        responseDto.setContent(message.getContent());
        responseDto.setSenderUsername(message.getSender().getName());
        responseDto.setRoomChatId(message.getRoomChat().getId());
        responseDto.setTimestamp(message.getTimestamp());
        // Gửi tin nhắn tới tất cả client đang lắng nghe kênh /topic/chat/{roomId}
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, responseDto);
    }

    // Lấy danh sách tin nhắn trong phòng
    @GetMapping("/{roomId}")
    public List<MessageResponseDto> getMessages(@PathVariable Long roomId) {
        return messageService.getMessagesByRoomId(roomId);
    }
}