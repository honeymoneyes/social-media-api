package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.payload.responses.PersonResponse;
import com.projects.socialmediaapi.user.services.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.projects.socialmediaapi.swagger.values.ExampleValues.*;
import static com.projects.socialmediaapi.user.constants.FriendEndpointConstants.*;

@RestController
@RequestMapping(MAIN_FRIENDS)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@ApiResponse(responseCode = "401",
        description = "Неавторизованный доступ",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PersonResponse.class),
                examples = @ExampleObject(value = UNAUTHORIZED_ACCESS)

        ))
@ApiResponse(responseCode = "401.1",
        description = "Неавторизованный доступ",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PersonResponse.class),
                examples = @ExampleObject(value = JWT_TOKEN_EXPIRED)

        ))
@ApiResponse(responseCode = "401.2",
        description = "Неавторизованный доступ",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PersonResponse.class),
                examples = @ExampleObject(value = JWT_TOKEN_NOT_EXIST)

        ))
@ApiResponse(responseCode = "404.1",
        description = "Пользователь не найден",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PersonResponse.class),
                examples = @ExampleObject(value = USER_NOT_FOUND)

        ))
@ApiResponse(responseCode = "409",
        description = "Вы не можете подписаться/отписаться на/от себя",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FriendShipResponse.class),
                examples = @ExampleObject(value = SELF_FOLLOW_UNFOLLOW_ERROR)

        ))
@Tag(name = "Друзья", description = "Управление друзьями и подписчиками пользователей")
public class FriendShipController {
    // -----------------------------------------------------------------------------------------------------------------

    private final FriendshipService friendShipService;

    // -----------------------------------------------------------------------------------------------------------------
    @GetMapping(SHOW_FRIENDS)
    @Operation(summary = "Показать друзей",
            description = "Получить список друзей пользователя.")

    public ResponseEntity<Set<PersonResponse>> performShowFriends(@PathVariable("userId") Long id) {
        return ResponseEntity.ok().body(friendShipService.showFriends(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping(SHOW_SUBSCRIBERS)
    @Operation(summary = "Показать подписчиков",
            description = "Получить список подписчиков пользователя.")
    public ResponseEntity<Set<PersonResponse>> performShowSubscribers(@PathVariable("userId") Long id) {
        return ResponseEntity.ok().body(friendShipService.showSubscribers(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(FOLLOW)
    @Operation(summary = "Подписаться",
            description = "Подписаться на аккаунт пользователя.")
    @ApiResponse(responseCode = "200",
            description = "Вы успешно подписались",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = FOLLOW_SUCCESS)

            ))
    @ApiResponse(responseCode = "409",
            description = "Вы уже являетесь подписчиком",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = SUBSCRIBER_ALREADY_EXIST)

            ))
    public ResponseEntity<FriendShipResponse> performFollow(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.follow(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(UNFOLLOW)
    @Operation(summary = "Отписаться",
            description = "Отписаться от аккаунта пользователя.")
    @ApiResponse(responseCode = "200",
            description = "Вы успешно отписались",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = UNFOLLOW_SUCCESS)

            ))
    @ApiResponse(responseCode = "404",
            description = "Вы успешно отписались",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = SUBSCRIBER_NOT_FOUND)

            ))
    public ResponseEntity<FriendShipResponse> performUnfollow(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.unfollow(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Operation(summary = "Принять запрос в друзья",
            description = "Принять запрос в друзья от пользователя.")
    @ApiResponse(responseCode = "200",
            description = "Вы успешно приняли заявку в друзья",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = REQUEST_FRIEND_SUCCESS)

            ))
    @ApiResponse(responseCode = "404",
            description = "Пользователь не подписан на вас",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = FRIENDSHIP_REQUEST_NOT_FOUND)

            ))
    @PostMapping(ACCEPT_REQUEST_FRIEND)
    public ResponseEntity<FriendShipResponse> performAcceptRequest(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.acceptRequest(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(REJECT_REQUEST_FRIEND)
    @Operation(summary = "Отклонить запрос в друзья",
            description = "Отклонить запрос в друзья от пользователя.")
    @ApiResponse(responseCode = "200",
            description = "Запрос успешно отклонен",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = FRIENDSHIP_REQUEST_REJECTED)

            ))
    @ApiResponse(responseCode = "404",
            description = "Пользователь не подписан на вас",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = SUBSCRIBER_NOT_FOUND)

            ))
    public ResponseEntity<FriendShipResponse> performRejectRequest(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.rejectRequest(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @DeleteMapping(REMOVE_FRIEND)
    @Operation(summary = "Удалить друга",
            description = "Удалить пользователя из списка друзей.")
    @ApiResponse(responseCode = "200",
            description = "Друг успешно удален",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = FRIEND_REMOVE_SUCCESS)

            ))
    @ApiResponse(responseCode = "404",
            description = "Друг не найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = FRIEND_IS_NOT_FOUND)

            ))
    public ResponseEntity<FriendShipResponse> performRemoveFriend(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.removeFriend(id));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
