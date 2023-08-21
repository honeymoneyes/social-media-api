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

@RestController
@RequiredArgsConstructor
@RequestMapping(MAIN_AUTH)
@Tag(
        name = "Контроллер аутентификации",
        description = "Контроллер для регистрации, входа и обновления токенов аутентификации")
public class AuthController {
    // -----------------------------------------------------------------------------------------------------------------

    private final AuthServiceImpl authServiceImpl;

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(SIGN_UP)
    @Operation(
            summary = "Регистрация пользователя",
            description = "Регистрация нового пользователя в системе")
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь успешно зарегистрирован",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"message\": \"User successfully registered!\"}")))
    @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 400, \"error\": \"NOT_VALID\", \"message\": [\n" +
                                                      "        \"username: Username shouldn't be empty\",\n" +
                                                      "        \"password: Password shouldn't be empty\",\n" +
                                                      "        \"email: Email address has invalid format\",\n" +
                                                      "        \"email: Email shouldn't be empty\"\n" +
                                                      "    ], \"timestamp\": \"08-21-2023 06:16:06\"}")))
    public ResponseEntity<MessageResponse> performRegister(@Valid
                                                           @RequestBody
                                                           RegisterRequest request) {
        return ResponseEntity.ok(authServiceImpl.register(request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(SIGN_IN)
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Вход зарегистрированного пользователя в систему")
    @ApiResponse(
            responseCode = "200",
            description = "Вход выполнен успешно",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(value = "{\"type\": \"Bearer \",\"accessToken\": \"eyJhbGciOiJIUzI1NiJ9." +
                                                      "eyJwYXNzd29yZCI6IiQyYSQxMCRTU0x1QlhGRkpna1MwaHdsMTBScHQuWi9qNk" +
                                                      "dMSFpHNm5SVzJKekRoTFlldWFKQUp5aHpKcSIsImlkIjoxLCJlbWFpbCI6InVz" +
                                                      "ZXIyQGdtYWlsLmNvbSIsInVzZXJuYW1lIjoidXNlcjIiLCJqdGkiOiIzOWE3NT" +
                                                      "AyMy1mZDZhLTQ4MWEtODA5OC04MDMxNTkyNWNhNjAiLCJzdWIiOiJ1c2VyMkBn" +
                                                      "bWFpbC5jb20iLCJpc3MiOiJob25leW1vbmV5IiwiaWF0IjoxNjkyNTk3Nzg3LCJ" +
                                                      "leHAiOjE2OTI1OTg5ODd9.nA0AmMf3bIiQLa1PhrmYbIKmlcnA36HW63B_DjDCt" +
                                                      "bQ\", " +
                                                      "\"refreshToken\": \"2b83d6c0-5f4a-4435-88b8-9a0665cf7b5a\"}")))
    @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 400, \"error\": \"NOT_VALID\", \"message\": [\n" +
                                                      "        \"email: Email address has invalid format\",\n" +
                                                      "        \"password: Password shouldn't be empty\",\n" +
                                                      "        \"email: Email shouldn't be empty\"\n" +
                                                      "    ], \"timestamp\": \"08-21-2023 06:16:06\"}")))
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 404, \"error\": \"USER_NOT_EXIST\", \"message\": [\n" +
                                                      "        \"User is not found!\"\n" +
                                                      "    ], \"timestamp\": \"08-21-2023 06:16:06\"}")))
    @ApiResponse(
            responseCode = "409",
            description = "Повторная авторизация",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject(value = "{\"status\": 409, \"error\": \"DUPLICATE_LOGIN\", \"message\": " +
                                                      "[\n" +
                                                      "        \"Login already done.\"\n" +
                                                      "    ], \"timestamp\": \"08-21-2023 06:16:06\"}")))
    public ResponseEntity<JwtResponse> performLogin(@Valid
                                                    @Parameter(description = "Запрос с данными для входа")
                                                    @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authServiceImpl.login(request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(REFRESH_TOKEN)
    @Operation(
            summary = "Обновление токена",
            description = "Обновление access и refresh токенов аутентификации")
    @ApiResponse(responseCode = "200",
            description = "Токен обновлен успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenRefreshResponse.class),
                    examples = @ExampleObject(
                            value = "{\"accessToken\": \"exampleNewToken\", \"refreshToken\": \"exampleNewRefreshToken\"}")))
    @ApiResponse(responseCode = "400", description = "Неверный запрос",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<TokenRefreshResponse> performRefresh(@Valid
                                                               @Parameter(description = "Запрос на обновление токена")
                                                               @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authServiceImpl.refresh(request));
    }

    // -----------------------------------------------------------------------------------------------------------------
}