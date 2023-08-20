package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.projects.socialmediaapi.security.constants.TokenConstants.DATE_TIME_FORMAT;
import static com.projects.socialmediaapi.user.constants.UserConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserInteractionService {

    private final PersonRepository personRepository;

    public Result getLoggedUserAndOtherUser(Long userId) {
        PersonDetails personDetails = getPersonDetails();

        Person loggedInPerson = personRepository
                .findById(personDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Person otherPerson = personRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return new Result(loggedInPerson, otherPerson);
    }

    public Person getLoggedUser() {
        PersonDetails personDetails = getPersonDetails();

        return personRepository
                .findById(personDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    private static PersonDetails getPersonDetails() {
        return (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public record Result(Person loggedInPerson, Person otherPerson) {
    }

    public static LocalDateTime getTimestamp(LocalDateTime time) {
        return LocalDateTime.parse(time
                        .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }
}
