package com.projects.socialmediaapi.user.repositories;

import com.projects.socialmediaapi.user.models.Friendship;
import com.projects.socialmediaapi.user.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query(value = "SELECT fr FROM Friendship fr WHERE (fr.sender =?1 AND fr.receiver = " +
                   "?2) OR (fr.sender =?2 AND fr.receiver =?1)")
    Optional<Friendship> findBySenderAndReceiver(Person sender, Person receiver);

}
