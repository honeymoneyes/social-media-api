package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.requests.PostRequest;
import com.projects.socialmediaapi.user.payload.responses.DeletePostResponse;
import com.projects.socialmediaapi.user.payload.responses.PostResponse;
import com.projects.socialmediaapi.user.payload.responses.UpdatePostResponse;
import com.projects.socialmediaapi.user.payload.responses.UploadPostResponse;
import com.projects.socialmediaapi.user.services.impl.ImageServiceImpl;
import com.projects.socialmediaapi.user.services.impl.PostServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.projects.socialmediaapi.swagger.DescriptionConstants.POST_TAG_DESCRIPTION;
import static com.projects.socialmediaapi.swagger.values.ExampleValues.*;
import static com.projects.socialmediaapi.user.constants.PostEndpointConstants.*;

@RestController
@RequestMapping(MAIN_POSTS)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(
        name = "Социальные Посты",
        description = POST_TAG_DESCRIPTION
)
public class PostController {

    // -----------------------------------------------------------------------------------------------------------------

    private final PostServiceImpl postServiceImpl;
    private final ImageServiceImpl imageServiceImpl;

    // -----------------------------------------------------------------------------------------------------------------
    @ApiResponse(
            responseCode = "200",
            description = "Возвращен список постов",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostResponse.class),
                    examples = @ExampleObject(value = POSTS_RETURN_SUCCESS)
            ))
    @GetMapping(SHOW_ALL_POSTS)
    @Operation(
            summary = "Просмотр всех постов пользователя по его ID",
            description = "Позволяет просматривать все посты пользователя по его ID")
    public ResponseEntity<PostResponse> performShowAllPosts(@Parameter(description = "ID пользователя для получения " +
                                                                                     "всех постов")
                                                            @PathVariable("userId")
                                                            Long postId) {
        return ResponseEntity.ok(postServiceImpl.showAllPostsByUserId(postId));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ApiResponse(
            responseCode = "200",
            description = "Возвращено изображение поста по ID",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Resource.class),
                    examples = @ExampleObject(value = IMAGE_RETURN_SUCCESS)
            ))
    @GetMapping(SHOW_IMAGE_OF_POST)
    @Operation(
            summary = "Просмотр изображения поста по ID",
            description = "Позволяет просматривать изображения постов по ID изображения")
    public ResponseEntity<Resource> performShowImage(@Parameter(description = "ID поста")
                                                     @PathVariable("postId")
                                                     Long postId) {
        return imageServiceImpl.showImageByPostId(postId);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(CREATE_POST)
    @Operation(summary = "Создание нового поста", description = "Создание постов")
    @ApiResponse(
            responseCode = "200",
            description = "Пост создан",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UploadPostResponse.class),
                    examples = @ExampleObject(value = POST_CREATED)
            ))
    @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UploadPostResponse.class),
                    examples = @ExampleObject(value = POST_REQUEST_NOT_VALID)
            ))
    public ResponseEntity<UploadPostResponse> performCreatePost(@Valid
                                                                @ModelAttribute
                                                                @Parameter(description = "Данные поста")
                                                                PostRequest request) {
        return ResponseEntity.ok(postServiceImpl.createPost(request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ApiResponse(
            responseCode = "200",
            description = "Пост обновлен",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UpdatePostResponse.class),
                    examples = @ExampleObject(value = POST_UPDATED)
            ))
    @ApiResponse(
            responseCode = "401",
            description = "Неавторизованный доступ",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UpdatePostResponse.class),
                    examples = @ExampleObject(value = UNAUTHORIZED_POST_ACTION)
            ))
    @ApiResponse(
            responseCode = "404",
            description = "Пост не существует",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UpdatePostResponse.class),
                    examples = @ExampleObject(value = POST_NOT_FOUND)
            ))
    @PatchMapping(UPDATE_POST)
    @Operation(summary = "Обновление поста по ID", description = "Обновление постов по переданному ID")
    public ResponseEntity<UpdatePostResponse> performUpdatePost(@Valid
                                                                @Parameter(description = "Данные поста")
                                                                @ModelAttribute PostRequest request,
                                                                @Parameter(description = "ID обновляемого поста")
                                                                @PathVariable("postId") Long id) throws IOException {
        return ResponseEntity.ok(postServiceImpl.updatePost(request, id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ApiResponse(
            responseCode = "200",
            description = "Пост удален",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UpdatePostResponse.class),
                    examples = @ExampleObject(value = POST_DELETED)
            ))
    @ApiResponse(
            responseCode = "401",
            description = "Неавторизованный доступ",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UpdatePostResponse.class),
                    examples = @ExampleObject(value = UNAUTHORIZED_POST_ACTION)
            ))
    @ApiResponse(
            responseCode = "404",
            description = "Пост не существует",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UpdatePostResponse.class),
                    examples = @ExampleObject(value = POST_NOT_FOUND)
            ))
    @DeleteMapping(DELETE_POST)
    @Operation(summary = "Удаление поста по ID", description = "Удаление постов по переданному ID")
    public ResponseEntity<DeletePostResponse> performDeletePost(@Parameter(description = "ID удаляемого поста")
                                                                @PathVariable("postId") Long id) {
        return ResponseEntity.ok(postServiceImpl.deletePost(id));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
