package com.projects.socialmediaapi.post.services;

import com.projects.socialmediaapi.post.exceptions.FilePathInvalidException;
import com.projects.socialmediaapi.post.exceptions.ImageNotFoundException;
import com.projects.socialmediaapi.post.models.Image;
import com.projects.socialmediaapi.post.models.Post;
import com.projects.socialmediaapi.post.payload.requests.PostRequest;
import com.projects.socialmediaapi.post.repositories.ImageRepository;
import com.projects.socialmediaapi.post.repositories.PostRepository;
import com.projects.socialmediaapi.user.models.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.projects.socialmediaapi.post.constants.PostConstants.IMAGE_NOT_FOUND;
import static com.projects.socialmediaapi.post.constants.PostConstants.INVALID_FILE_PATH;

@Service
@RequiredArgsConstructor
public class ImageService {
    // -----------------------------------------------------------------------------------------------------------------

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    // -----------------------------------------------------------------------------------------------------------------

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
