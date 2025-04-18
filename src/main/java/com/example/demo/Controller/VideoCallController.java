package com.example.demo.Controller;

import com.example.demo.Model.SignalingMessage;
import lombok.*;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Controller
public class VideoCallController {
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, Set<String>> roomUsers = new ConcurrentHashMap<>();
    private final Map<String, String> sessionToUserMap = new ConcurrentHashMap<>();

    public VideoCallController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/video.join/{roomId}")
    public void joinRoom(@DestinationVariable String roomId, @org.springframework.messaging.handler.annotation.Payload SignalingMessage message, SimpMessageHeaderAccessor headerAccessor) {

        String username = message.getFrom();
        String sessionId = headerAccessor.getSessionId();

        // Thêm người dùng vào phòng và lưu ánh xạ sessionId -> username
        roomUsers.computeIfAbsent(roomId, k -> Collections.newSetFromMap(new ConcurrentHashMap<>())).add(username);
        sessionToUserMap.put(sessionId, username);

        // Lấy danh sách người dùng trong phòng, loại bỏ chính người gửi
        Set<String> usersInRoom = roomUsers.getOrDefault(roomId, Collections.emptySet());
        SignalingMessage joinMessage = new SignalingMessage("JOIN", username, null, null);

        // Gửi tin nhắn JOIN tới tất cả người dùng trong phòng, trừ người gửi
        for (String user : usersInRoom) {
            if (!user.equals(username)) { // Loại bỏ người gửi
                messagingTemplate.convertAndSendToUser(user, "/queue/video", joinMessage);
            }
        }
        messagingTemplate.convertAndSend("/topic/video/" + roomId,
                new SignalingMessage("JOIN", username, null, null));

        messagingTemplate.convertAndSendToUser(username, "/queue/video",
                new SignalingMessage("USER_LIST", "server", username, new ArrayList<>(roomUsers.get(roomId))));
    }

    @MessageMapping("/video.offer/{roomId}")
    public void sendOffer(@DestinationVariable String roomId, @Payload SignalingMessage message) {
        String sender = message.getFrom();
        Set<String> usersInRoom = roomUsers.getOrDefault(roomId, Collections.emptySet());

        System.out.println("Sending OFFER from " + sender + " to all users in room except sender");

        // Gửi tin nhắn OFFER tới tất cả người dùng trong phòng, trừ người gửi
        for (String user : usersInRoom) {
            if (!user.equals(sender)) { // Loại bỏ người gửi
                message.setTo(user); // Cập nhật trường 'to' trong tin nhắn
                messagingTemplate.convertAndSendToUser(user, "/queue/video", message);
            }
        }        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/video", message);
    }

    @MessageMapping("/video.answer/{roomId}")
    public void sendAnswer(@DestinationVariable String roomId, @org.springframework.messaging.handler.annotation.Payload SignalingMessage message) {
        System.out.println("Sending ANSWER from " + message.getFrom() + " to " + message.getTo());
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/video", message);
    }

    @MessageMapping("/video.candidate/{roomId}")
    public void sendCandidate(@DestinationVariable String roomId, @org.springframework.messaging.handler.annotation.Payload SignalingMessage message) {
        System.out.println("Sending CANDIDATE from " + message.getFrom() + " to " + message.getTo());
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/video", message);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String username = sessionToUserMap.get(sessionId);
        if (username != null) {
            roomUsers.values().forEach(users -> users.remove(username));
            sessionToUserMap.remove(sessionId);
            messagingTemplate.convertAndSend("/topic/video",
                    new SignalingMessage("LEAVE", username, null, null));
        }
    }
}
