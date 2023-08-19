package com.projects.socialmediaapi.user.repositories;

import com.projects.socialmediaapi.user.models.FriendshipRequest;
import com.projects.socialmediaapi.user.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipRequest, Long> {
    Optional<FriendshipRequest> findBySenderAndReceiver(Person sender, Person receiver);
}
