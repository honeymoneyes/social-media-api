package com.projects.socialmediaapi.post.services;

import com.projects.socialmediaapi.post.exceptions.ImageNotFoundException;
import com.projects.socialmediaapi.post.models.Image;
import com.projects.socialmediaapi.post.models.Post;
import com.projects.socialmediaapi.post.payload.requests.PostRequest;
import com.projects.socialmediaapi.post.payload.responses.ImageResponse;
import com.projects.socialmediaapi.post.payload.responses.PostResponse;
import com.projects.socialmediaapi.post.payload.responses.UploadPostResponse;
import com.projects.socialmediaapi.post.repositories.ImageRepository;
import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import com.projects.socialmediaapi.utils.mappers.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

import static com.projects.socialmediaapi.post.constants.PostConstants.IMAGE_NOT_FOUND;
import static com.projects.socialmediaapi.user.constants.UserConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final PersonRepository personRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public UploadPostResponse createPost(PostRequest request) throws IOException {

        Person person = getAuthenticatePerson();

        Image image = imageService.createPostWithImageAndReturnImage(request.getTitle(), request.getBody(), request.getImage(),
                person);

        String fileDownloadUri = getFileDownloadUri(image);

        return getUploadPostResponse(request.getTitle(), request.getBody(), request.getImage(), image, fileDownloadUri);
    }

    private Person getAuthenticatePerson() {
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return personRepository.findByEmail(personDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    private static String getFileDownloadUri(Image image) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(image.getId()))
                .toUriString();
    }


    private static UploadPostResponse getUploadPostResponse(String title, String body, MultipartFile file, Image image, String fileDownloadUri) {
        return UploadPostResponse.builder()
                .title(title)
                .body(body)
                .imageResponse(
                        ImageResponse.builder()
                                .fileName(image.getFileName())
                                .contentType(image.getFileType())
                                .downloadUri(fileDownloadUri)
                                .size(file.getSize())
                                .build())
                .build();
    }

    public ResponseEntity<Resource> showImageByPostId(Long postId) {
        Image image = imageRepository
                .findById(postId)
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(new ByteArrayResource(image.getData()));
    }

    @Transactional
    public PostResponse showAllPostsByUserId(Long postId) {
        Person person = personRepository.findById(postId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        List<Post> posts = person.getPosts();
        return PostResponse.builder()
                .posts(posts)
                .build();
    }
}
