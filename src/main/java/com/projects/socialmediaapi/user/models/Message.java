package com.projects.socialmediaapi.user.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "sender_id",
            referencedColumnName = "id")
    private Person sender;
    @ManyToOne
    @JoinColumn(
            name = "receiver_id",
            referencedColumnName = "id")
    private Person receiver;
    private String text;
    private LocalDateTime timestamp;
}
