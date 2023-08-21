package com.projects.socialmediaapi.user.services.impl;

import com.projects.socialmediaapi.security.advice.ErrorDetails;
import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.SelfActionException;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

import static com.projects.socialmediaapi.security.constants.TokenConstants.DATE_TIME_FORMAT;
import static com.projects.socialmediaapi.user.constants.UserConstants.SELF_ACTION;
import static com.projects.socialmediaapi.user.constants.UserConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserInteractionServiceImpl {

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
                .message(Collections.singletonList(exception.getMessage()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static Supplier<UserNotFoundException> UserNotFoundException() {
        return () -> new UserNotFoundException(USER_NOT_FOUND);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Result checkUsersAreNotSameUserAndGetThem(Long userId) {
        Result result = getLoggedUserAndOtherUser(userId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Check loggedInPerson and other person aren't same user*/
    public static void areIdsFromSameUser(Person loggedInPerson, Person otherPerson) {
        if (Objects.equals(loggedInPerson.getId(), otherPerson.getId())) {
            throw new SelfActionException(SELF_ACTION);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------


    public static Person loggedInPerson(Result result) {
        return result.loggedInPerson();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static Person otherPerson(Result result) {
        return result.otherPerson();
    }

    // -----------------------------------------------------------------------------------------------------------------
}
