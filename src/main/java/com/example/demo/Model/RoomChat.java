package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_chat")
public class RoomChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @JsonIgnore
    private String password;

    private boolean privateRoom;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    @JsonIgnoreProperties({"joinedRooms", "createdRooms"}) // tránh vòng lặp
    private User creator;

    // Danh sách người tham gia
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "room_participants",
            joinColumns = @JoinColumn(name = "room_chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference(value = "room-participants")
    @JsonIgnore
    private Set<User> participants = new HashSet<>();

    // Danh sách tin nhắn
    @OneToMany(mappedBy = "roomChat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "room-messages")
    @JsonIgnore
    private Set<Message> messages = new HashSet<>();
    public void addParticipant(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        participants.add(user);
        user.getJoinedRooms().add(this);
    }

    public void addMessage(Message message) {
        messages.add(message);
        message.setRoomChat(this);
    }
}