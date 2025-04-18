package com.example.demo.Controller;

import com.example.demo.DTO.RoomChatDto;
import com.example.demo.Model.RoomChat;
import com.example.demo.Request.JoinRoomRequest;
import com.example.demo.Service.RoomChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomChatController {
    private RoomChatService roomChatService;
    public RoomChatController(RoomChatService roomChatService) {
        this.roomChatService = roomChatService;
    }

    @PostMapping("")
    public ResponseEntity<RoomChat> createRoom(@RequestBody RoomChatDto room, Authentication authentication) {
        String username  = authentication.getName();
        System.out.println(room.isPrivate());
        return ResponseEntity.ok(roomChatService.createRoom(room, username));
    }

    @GetMapping("")
    public ResponseEntity<List<RoomChat>> getRooms() {
        return ResponseEntity.ok(roomChatService.getAllRooms());
    }

    // Tham gia phòng bằng mã code
    @PostMapping("/join")
    public ResponseEntity<RoomChat> joinRoom(@RequestBody JoinRoomRequest joinRoomRequest, Authentication authentication) {
        String username = authentication.getName();
        RoomChat roomChat = roomChatService.joinRoom(joinRoomRequest, username);
        return ResponseEntity.ok(roomChat);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomChat> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomChatService.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found")));
    }


    @GetMapping("/joined")
    public ResponseEntity<List<RoomChat>> getJoinedRooms(Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Authentication: " + username);

        List<RoomChat> joinedRooms = roomChatService.getJoinedRooms(username);
        return ResponseEntity.ok(joinedRooms);
    }

    // Lấy danh sách phòng mà user đã tạo
    @GetMapping("/created")
    public ResponseEntity<List<RoomChat>> getCreatedRooms(Authentication authentication) {
        String username = authentication.getName();
        List<RoomChat> createdRooms = roomChatService.getCreatedRooms(username);
        return ResponseEntity.ok(createdRooms);
    }
}

