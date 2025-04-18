package com.example.demo.Model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignalingMessage {
    private String type;
    private String from;
    private String to;
    private Object data;
}