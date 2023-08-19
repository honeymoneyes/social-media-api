package com.projects.socialmediaapi.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.socialmediaapi.user.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "sender", "receiver"})
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"sender", "receiver"})
public class FriendshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private Person sender;
    @ManyToOne
    @JsonIgnore
    private Person receiver;
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
}
