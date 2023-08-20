package com.projects.socialmediaapi.user.repositories;

import com.projects.socialmediaapi.user.models.Message;
import com.projects.socialmediaapi.user.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // -----------------------------------------------------------------------------------------------------------------

    @Query("SELECT msg FROM Message msg WHERE (msg.sender =?1 AND msg.receiver =?2) OR (msg.receiver =?1 AND msg" +
           ".sender =?2) ORDER BY msg.timestamp")
    Optional<List<Message>> findChat(Person sender,
                                     Person receiver);

    // -----------------------------------------------------------------------------------------------------------------
}
