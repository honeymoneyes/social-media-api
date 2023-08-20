package com.projects.socialmediaapi.user.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "post"})
@ToString(exclude = {"post"})
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Lob
    @JsonIgnore
    private byte[] data;
    @OneToOne
    @JoinColumn(name = "image_id",
            referencedColumnName = "id")
    @JsonBackReference
    private Post post;
}
