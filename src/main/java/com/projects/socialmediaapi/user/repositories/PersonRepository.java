package com.projects.socialmediaapi.user.repositories;

import com.projects.socialmediaapi.user.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String username);
    boolean existsByEmail(String email);

    Optional<List<Person>> findBySubscribersContaining(Person person);
}
