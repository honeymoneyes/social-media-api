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
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.parseMediaType;

@Service
@RequiredArgsConstructor
public class ImageService {

    // -----------------------------------------------------------------------------------------------------------------

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public Image getImage(PostRequest request,
                          Person person) {
        String fileName = getFileName(request.getImage());
        isFilepathValid(fileName);
        return createPostWithImageAndReturnImage(request, person, fileName);
    }

    private Image createPostWithImageAndReturnImage(PostRequest request, Person person, String fileName) {
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
                .cleanPath(getCleanPath(file));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String getCleanPath(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename());
    }

    // -----------------------------------------------------------------------------------------------------------------

    public ResponseEntity<Resource> showImageByPostId(Long postId) {
        Image image = getImageById(postId);
        return transformImageBytesToImage(image);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static ResponseEntity<Resource> transformImageBytesToImage(Image image) {
        return ResponseEntity.ok()
                .contentType(parseMediaType(getFileType(image)))
                .header(CONTENT_DISPOSITION,
                        "attachment; filename=\"" + getFileName(image) + "\"")
                .body(getImage(image));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static ByteArrayResource getImage(Image image) {
        return new ByteArrayResource(image.getData());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String getFileName(Image image) {
        return image.getFileName();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String getFileType(Image image) {
        return image.getFileType();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Image getImageById(Long postId) {
        return imageRepository
                .findById(postId)
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Post createPost(PostRequest request, Person person) {
        return Post.builder()
                .title(getTitle(request))
                .body(transformImageBytesToImage(request))
                .timestamp(getTimestamp(LocalDateTime.now()))
                .person(person)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String transformImageBytesToImage(PostRequest request) {
        return request.getBody();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String getTitle(PostRequest request) {
        return request.getTitle();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Image createImage(PostRequest request, String fileName, Post post){
        try {
            return Image.builder()
                    .fileName(fileName)
                    .fileType(getContentType(request))
                    .data(getData(request))
                    .post(post)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static byte[] getData(PostRequest request) throws IOException {
        return request.getImage().getBytes();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String getContentType(PostRequest request) {
        return request.getImage().getContentType();
    }

    // -----------------------------------------------------------------------------------------------------------------
}
