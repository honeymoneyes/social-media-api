package com.projects.socialmediaapi.post.repositories;

import com.projects.socialmediaapi.post.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {
}
