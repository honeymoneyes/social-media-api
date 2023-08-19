package com.projects.socialmediaapi.user.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projects.socialmediaapi.user.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FriendshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonBackReference
    private Person sender;
    @ManyToOne
    @JsonBackReference
    private Person receiver;
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
}
