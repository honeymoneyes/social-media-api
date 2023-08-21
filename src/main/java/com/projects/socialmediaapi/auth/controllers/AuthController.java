package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.auth.payload.requests.LoginRequest;
import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.auth.payload.responses.MessageResponse;
import com.projects.socialmediaapi.auth.services.impl.AuthServiceImpl;
import com.projects.socialmediaapi.security.payload.requests.TokenRefreshRequest;
import com.projects.socialmediaapi.security.payload.responses.JwtResponse;
import com.projects.socialmediaapi.security.payload.responses.TokenRefreshResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.projects.socialmediaapi.security.constants.SecurityEndpointConstants.*;
import static com.projects.socialmediaapi.swagger.DescriptionConstants.*;
import static com.projects.socialmediaapi.swagger.values.ExampleValues.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(MAIN_AUTH)
@Tag(
        name = "Аутентификация",
        description = AUTH_TAG_DESCRIPTION)
public class AuthController {
    // -----------------------------------------------------------------------------------------------------------------

    private final AuthServiceImpl authServiceImpl;

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(SIGN_UP)
    @Operation(
            summary = "Регистрация пользователя",
            description = AUTH_SIGN_UP_DESCRIPTION)
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь успешно зарегистрирован",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = REGISTER_OK)
            ))

    @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = REGISTER_NOT_VALID)
            ))
    @ApiResponse(
            responseCode = "409",
            description = "Пользователь уже существует",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = USER_ALREADY_EXIST)
            ))
    public ResponseEntity<MessageResponse> performRegister(@Valid
                                                           @RequestBody
                                                           @Parameter(description = "Запрос с данными для регистрации")
                                                           RegisterRequest request) {
        return ResponseEntity.ok(authServiceImpl.register(request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(SIGN_IN)
    @Operation(
            summary = "Аутентификация пользователя",
            description = AUTH_SIGN_IN_DESCRIPTION)
    @ApiResponse(
            responseCode = "200",
            description = "Вход выполнен успешно",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(value = LOGIN_OK)
            ))

    @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(value = LOGIN_NOT_VALID)
            ))

    @ApiResponse(
            responseCode = "400.1",
            description = "Неверный запрос",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(value = BAD_CREDENTIALS)
            ))

    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(value = USER_NOT_FOUND)
            ))

    @ApiResponse(
            responseCode = "409",
            description = "Повторная авторизация",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(value = DUPLICATE_LOGIN)
            ))
    public ResponseEntity<JwtResponse> performLogin(@Valid
                                                    @Parameter(description = "Запрос с данными для входа")
                                                    @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authServiceImpl.login(request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(REFRESH_TOKEN)
    @Operation(
            summary = "Обновление токена",
            description = AUTH_REFRESH_DESCRIPTION)
    @ApiResponse(responseCode = "200",
            description = "Токен обновлен успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenRefreshResponse.class),
                    examples = @ExampleObject(value = REFRESH_TOKEN_UPDATED)
            ))

    @ApiResponse(responseCode = "400",
            description = "Неверный запрос",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenRefreshResponse.class),
                    examples = @ExampleObject(value = REFRESH_TOKEN_NOT_VALID)
            ))
    @ApiResponse(responseCode = "401",
            description = "Неавторизованный доступ",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenRefreshResponse.class),
                    examples = @ExampleObject(value = REFRESH_TOKEN_EXPIRED)


            ))
    @ApiResponse(responseCode = "404",
            description = "Токен не найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenRefreshResponse.class),
                    examples = @ExampleObject(value = REFRESH_TOKEN_NOT_EXIST)

            ))
    public ResponseEntity<TokenRefreshResponse> performRefresh(@Valid
                                                               @Parameter(description = "Запрос на обновление токена")
                                                               @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authServiceImpl.refresh(request));
    }

    // -----------------------------------------------------------------------------------------------------------------
}