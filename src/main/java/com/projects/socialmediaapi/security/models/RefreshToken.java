package com.projects.socialmediaapi.security.models;

import com.projects.socialmediaapi.user.models.Person;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Instant expirationDate;
    @OneToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id")
    private Person person;
}
