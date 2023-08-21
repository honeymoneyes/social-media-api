package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.security.advice.ErrorDetails;
import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Supplier;

import static com.projects.socialmediaapi.security.constants.TokenConstants.DATE_TIME_FORMAT;
import static com.projects.socialmediaapi.user.constants.UserConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserInteractionService {

    // -----------------------------------------------------------------------------------------------------------------

    private final PersonRepository personRepository;

    // -----------------------------------------------------------------------------------------------------------------

    public Result getLoggedUserAndOtherUser(Long userId) {
        PersonDetails personDetails = getPersonDetails();

        Person loggedInPerson = personRepository
                .findById(personDetails.getId())
                .orElseThrow(UserNotFoundException());

        Person otherPerson = personRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException());
        return new Result(loggedInPerson, otherPerson);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Person getLoggedUser() {
        PersonDetails personDetails = getPersonDetails();

        return personRepository
                .findById(personDetails.getId())
                .orElseThrow(UserNotFoundException());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static PersonDetails getPersonDetails() {
        return (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public record Result(Person loggedInPerson, Person otherPerson) {
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static LocalDateTime getTimestamp(LocalDateTime time) {
        return LocalDateTime.parse(time
                        .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static String getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                .format(LocalDateTime.now());
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static ErrorDetails getErrorDetails(HttpStatus httpStatus,
                                               String errorStatus,
                                               Exception exception) {
        return ErrorDetails.builder()
                .status(httpStatus.value())
                .error(errorStatus)
                .timestamp(getDateTimeFormatter())
                .message(exception.getMessage())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static Supplier<UserNotFoundException> UserNotFoundException() {
        return () -> new UserNotFoundException(USER_NOT_FOUND);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
