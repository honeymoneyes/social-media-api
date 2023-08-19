package com.projects.socialmediaapi.user.repositories;

import com.projects.socialmediaapi.user.models.Message;
import com.projects.socialmediaapi.user.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<List<Message>> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestamp(Person sender1,
                                                                                        Person receiver1,
                                                                                        Person sender2,
                                                                                        Person receiver2);
}
