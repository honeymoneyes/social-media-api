package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.requests.TextMessageRequest;
import com.projects.socialmediaapi.user.payload.responses.TextMessageResponse;
import com.projects.socialmediaapi.user.services.impl.MessageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.projects.socialmediaapi.swagger.DescriptionConstants.MESSAGE_TAG_DESCRIPTION;
import static com.projects.socialmediaapi.swagger.values.ExampleValues.*;
import static com.projects.socialmediaapi.user.constants.MessageEndpointConstants.*;

@RestController
@RequestMapping(MAIN_MESSAGE)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(
        name = "Сообщения",
        description = MESSAGE_TAG_DESCRIPTION
)
@ApiResponse(
        responseCode = "404",
        description = "Переписка с несуществующим пользователем недоступна",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TextMessageResponse.class),
                examples = @ExampleObject(value = USER_NOT_FOUND)
        ))
@ApiResponse(
        responseCode = "409",
        description = "Переписка с самим собой недоступна",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TextMessageResponse.class),
                examples = @ExampleObject(value = SELF_FOLLOW_UNFOLLOW_ERROR)
        ))
public class MessageController {

    private final MessageServiceImpl messageServiceImpl;

    @PostMapping(SEND_MESSAGE)
    @Operation(summary = "Отправка текстового сообщения",
            description = "Отправляет текстовое сообщение" +
                          " от авторизованного пользователя" +
                          " к указанному получателю.")
    @ApiResponse(
            responseCode = "200",
            description = "Сообщение отправлено",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TextMessageResponse.class),
                    examples = @ExampleObject(value = SEND_MESSAGE_SUCCESS)
            ))
    @ApiResponse(
            responseCode = "405",
            description = "Сообщение не отправлено",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TextMessageResponse.class),
                    examples = @ExampleObject(value = SEND_MESSAGE_DENIED)
            ))
    public ResponseEntity<TextMessageResponse> performSendMessage(
            @Parameter(description = "ID получателя")
            @PathVariable("userId") Long id,
            @RequestBody TextMessageRequest request) {
        return ResponseEntity.ok(messageServiceImpl.sendMessage(id, request));
    }

    @GetMapping(GET_CHAT)
    @Operation(summary = "Получение списка сообщений чата",
            description = "Получает список текстовых сообщений" +
                          " для чата между авторизованным пользователем" +
                          " и указанным собеседником.")
    @ApiResponse(
            responseCode = "200",
            description = "Переписка доступна",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TextMessageResponse.class),
                    examples = @ExampleObject(value = SHOW_CHAT_SUCCESS)
            ))
    @ApiResponse(
            responseCode = "405",
            description = "Переписка не доступна",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TextMessageResponse.class),
                    examples = @ExampleObject(value = SHOW_CHAT_DENIED)
            ))
    public ResponseEntity<List<TextMessageResponse>> performGetChat(
            @Parameter(description = "ID получателя с которым есть чат")
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(messageServiceImpl.getChat(userId));
    }
}
