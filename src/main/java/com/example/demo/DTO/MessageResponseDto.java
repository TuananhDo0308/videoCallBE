package com.example.demo.DTO;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseDto {
    private Long id;
    private String content;
    private String senderUsername;
    private Long roomChatId;
    private LocalDateTime timestamp;
}