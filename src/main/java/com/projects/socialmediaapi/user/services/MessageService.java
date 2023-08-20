package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.ChatNotAllowedException;
import com.projects.socialmediaapi.user.exceptions.ChatNotFoundException;
import com.projects.socialmediaapi.user.exceptions.MessageSendingNotAllowedException;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Message;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.payload.requests.TextMessageRequest;
import com.projects.socialmediaapi.user.payload.responses.TextMessageResponse;
import com.projects.socialmediaapi.user.repositories.MessageRepository;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.projects.socialmediaapi.security.constants.TokenConstants.DATE_TIME_FORMAT;
import static com.projects.socialmediaapi.user.constants.UserConstants.*;
import static com.projects.socialmediaapi.user.services.FriendshipService.areIdsFromSameUser;
import static com.projects.socialmediaapi.user.services.UserInteractionService.getTimestamp;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final PersonRepository personRepository;
    private final MessageRepository messageRepository;

    public TextMessageResponse sendMessage(Long id, TextMessageRequest request) {
        Result result = getLoggedInPersonAndOtherPerson(id);

        areIdsFromSameUser(result.loggedInPerson(), result.otherPerson());

        if (!isAreUsersFriends(result.loggedInPerson(), result.otherPerson())) {
            throw new MessageSendingNotAllowedException(MESSAGE_NOT_ALLOWED);
        } else {
            Message message = Message.builder()
                    .sender(result.loggedInPerson())
                    .receiver(result.otherPerson())
                    .text(request.getText())
                    .timestamp(getTimestamp(LocalDateTime.now()))
                    .build();

            messageRepository.save(message);
        }
        return TextMessageResponse.builder()
                .senderUsername(result.loggedInPerson().getUsername())
                .receiverUsername(result.otherPerson().getUsername())
                .text(request.getText())
                .timestamp(getTimestamp(LocalDateTime.now()))
                .build();
    }

    private Result getLoggedInPersonAndOtherPerson(Long id) {
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Person loggedInPerson = personRepository
                .findById(personDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Person otherPerson = personRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return new Result(loggedInPerson, otherPerson);
    }

    private record Result(Person loggedInPerson, Person otherPerson) {
    }

    public static boolean isAreUsersFriends(Person loggedInPerson, Person otherPerson) {
        return loggedInPerson
                .getFriends()
                .stream()
                .anyMatch(friend -> Objects.equals(friend.getId(), otherPerson.getId()));
    }

    public List<TextMessageResponse> getChat(Long userId) {
        Result result = getLoggedInPersonAndOtherPerson(userId);

        areIdsFromSameUser(result.loggedInPerson(), result.otherPerson());

        if (!isAreUsersFriends(result.loggedInPerson(), result.otherPerson())) {
            throw new ChatNotAllowedException(CHAT_NOT_ALLOWED);
        }

        List<Message> messages = getListMessagesOfPeople(result);

        return getTextMessageResponses(messages);
    }

    private static List<TextMessageResponse> getTextMessageResponses(List<Message> messages) {
        return messages.
                stream()
                .map(message -> TextMessageResponse.builder()
                        .senderUsername(message.getSender().getUsername())
                        .receiverUsername(message.getReceiver().getUsername())
                        .text(message.getText())
                        .timestamp(getTimestamp(message.getTimestamp()))
                        .build())
                .toList();
    }

    private List<Message> getListMessagesOfPeople(Result result) {
        return messageRepository.findChat(
                result.loggedInPerson,
                result.otherPerson)
                .orElseThrow(() -> new ChatNotFoundException(CHAT_NOT_FOUND));
    }
}
