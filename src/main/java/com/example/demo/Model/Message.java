package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    // Người gửi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonBackReference(value = "user-messages")
    private User sender;

    // Phòng chat thuộc về
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_chat_id", nullable = false)
    @JsonBackReference(value = "room-messages")
    private RoomChat roomChat;

    private LocalDateTime timestamp;
}
