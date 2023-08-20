package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.exceptions.FilePathInvalidException;
import com.projects.socialmediaapi.user.exceptions.ImageNotFoundException;
import com.projects.socialmediaapi.user.models.Image;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.models.Post;
import com.projects.socialmediaapi.user.payload.requests.PostRequest;
import com.projects.socialmediaapi.user.repositories.ImageRepository;
import com.projects.socialmediaapi.user.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.projects.socialmediaapi.user.constants.PostConstants.IMAGE_NOT_FOUND;
import static com.projects.socialmediaapi.user.constants.PostConstants.INVALID_FILE_PATH;
import static com.projects.socialmediaapi.user.services.UserInteractionService.getTimestamp;

@Service
@RequiredArgsConstructor
public class ImageService {

    // -----------------------------------------------------------------------------------------------------------------

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public Image createPostWithImageAndReturnImage(PostRequest request,
                                                   Person person) throws IOException {
        String fileName = getFileName(request.getImage());
        isFilepathValid(fileName);
        Post post = createPost(request, person);
        Image image = createImage(request, fileName, post);
        postRepository.save(post);
        return imageRepository.save(image);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void isFilepathValid(String fileName) {
        if (fileName.contains("..")) {
            throw new FilePathInvalidException(INVALID_FILE_PATH + fileName);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static String getFileName(MultipartFile file) {
        return StringUtils
                .cleanPath(Objects
                        .requireNonNull(file
                                .getOriginalFilename()));
    }

    // -----------------------------------------------------------------------------------------------------------------

    public ResponseEntity<Resource> showImageByPostId(Long postId) {
        Image image = imageRepository
                .findById(postId)
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getFileName() + "\"")
                .body(new ByteArrayResource(image.getData()));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Post createPost(PostRequest request, Person person) {
        return Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .timestamp(getTimestamp(LocalDateTime.now()))
                .person(person)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Image createImage(PostRequest request, String fileName, Post post) throws IOException {
        return Image.builder()
                .fileName(fileName)
                .fileType(request.getImage().getContentType())
                .data(request.getImage().getBytes())
                .post(post)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
}
