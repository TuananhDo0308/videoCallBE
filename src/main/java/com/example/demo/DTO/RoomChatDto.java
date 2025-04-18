package com.example.demo.DTO;


import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class RoomChatDto {
    private String name;

    @Nullable
    private String password;
    private boolean isPrivate;
}