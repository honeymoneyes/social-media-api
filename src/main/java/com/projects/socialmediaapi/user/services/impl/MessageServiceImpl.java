package com.projects.socialmediaapi.user.services.impl;

import com.projects.socialmediaapi.user.exceptions.ChatNotAllowedException;
import com.projects.socialmediaapi.user.exceptions.ChatNotFoundException;
import com.projects.socialmediaapi.user.exceptions.MessageSendingNotAllowedException;
import com.projects.socialmediaapi.user.models.Message;
import com.projects.socialmediaapi.user.payload.requests.TextMessageRequest;
import com.projects.socialmediaapi.user.payload.responses.TextMessageResponse;
import com.projects.socialmediaapi.user.repositories.MessageRepository;
import com.projects.socialmediaapi.user.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.projects.socialmediaapi.user.constants.UserConstants.*;
import static com.projects.socialmediaapi.user.services.impl.FriendshipServiceImpl.areUsersFriends;
import static com.projects.socialmediaapi.user.services.impl.UserInteractionServiceImpl.*;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    // -----------------------------------------------------------------------------------------------------------------

    private final MessageRepository messageRepository;
    private final UserInteractionServiceImpl userInteractionServiceImpl;

    // -----------------------------------------------------------------------------------------------------------------

    public TextMessageResponse sendMessage(Long userId, TextMessageRequest request) {
        Result result = userInteractionServiceImpl.checkUsersAreNotSameUserAndGetThem(userId);

        if (!areUsersFriends(result)) {
            throw new MessageSendingNotAllowedException(MESSAGE_NOT_ALLOWED);
        }

        Message message = getMessage(request, result);

        messageRepository.save(message);

        return getTextMessageResponse(message);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Message getMessage(TextMessageRequest request, Result result) {
        return Message.builder()
                .sender(loggedInPerson(result))
                .receiver(otherPerson(result))
                .text(request.getText())
                .timestamp(getTimestamp(LocalDateTime.now()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public List<TextMessageResponse> getChat(Long userId) {
        Result result = userInteractionServiceImpl.checkUsersAreNotSameUserAndGetThem(userId);

        if (!areUsersFriends(result)) {
            throw new ChatNotAllowedException(CHAT_NOT_ALLOWED);
        }

        List<Message> messages = getListMessagesOfPeople(result);

        return getListTextMessageResponses(messages);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static List<TextMessageResponse> getListTextMessageResponses(List<Message> messages) {
        return messages.
                stream()
                .map(MessageServiceImpl::getTextMessageResponse)
                .toList();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static TextMessageResponse getTextMessageResponse(Message message) {
        return TextMessageResponse.builder()
                .senderUsername(getSenderUsername(message))
                .receiverUsername(getReceiverUsername(message))
                .text(getMessageText(message))
                .timestamp(getTimestamp(getMessageTimestamp(message)))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static LocalDateTime getMessageTimestamp(Message message) {
        return message.getTimestamp();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String getMessageText(Message message) {
        return message.getText();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String getReceiverUsername(Message message) {
        return message.getReceiver().getUsername();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String getSenderUsername(Message message) {
        return message.getSender().getUsername();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private List<Message> getListMessagesOfPeople(Result result) {
        return messageRepository
                .findChat(loggedInPerson(result), otherPerson(result))
                .orElseThrow(() -> new ChatNotFoundException(CHAT_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
