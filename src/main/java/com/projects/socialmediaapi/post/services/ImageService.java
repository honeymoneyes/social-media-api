package com.projects.socialmediaapi.post.services;

import com.projects.socialmediaapi.post.exceptions.FilePathInvalidException;
import com.projects.socialmediaapi.post.models.Image;
import com.projects.socialmediaapi.post.models.Post;
import com.projects.socialmediaapi.post.repositories.ImageRepository;
import com.projects.socialmediaapi.post.repositories.PostRepository;
import com.projects.socialmediaapi.user.models.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    public Image createPostWithImageAndReturnImage(String title, String body, MultipartFile file, Person person) throws IOException {
        String fileName = StringUtils
                .cleanPath(Objects
                        .requireNonNull(file
                        .getOriginalFilename()));

        if (fileName.contains("..")) {
            throw new FilePathInvalidException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        Post post = createPost(title, body, person);
        Image image = createImage(file, fileName, post);

        postRepository.save(post);
        return imageRepository.save(image);
    }

    private static Post createPost(String title, String body, Person person) {
        return Post.builder()
                .title(title)
                .body(body)
                .person(person)
                .build();
    }

    private static Image createImage(MultipartFile file, String fileName, Post post) throws IOException {
        return Image.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .data(file.getBytes())
                .post(post)
                .build();
    }
}
