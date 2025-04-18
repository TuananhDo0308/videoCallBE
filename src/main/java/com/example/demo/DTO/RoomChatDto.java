package com.example.demo.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class RoomChatDto {
    private String name;

    @Nullable
    private String password;
    @JsonProperty("isPrivate")
    private boolean isPrivate;
}