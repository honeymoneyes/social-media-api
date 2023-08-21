package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.payload.requests.TextMessageRequest;
import com.projects.socialmediaapi.user.payload.responses.TextMessageResponse;

import java.util.List;

public interface MessageService {
    TextMessageResponse sendMessage(Long userId, TextMessageRequest request);

    List<TextMessageResponse> getChat(Long userId);
}
