package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.ImageNotFoundException;
import com.projects.socialmediaapi.user.exceptions.PostNotFoundException;
import com.projects.socialmediaapi.user.exceptions.UnauthorizedPostAction;
import com.projects.socialmediaapi.user.models.Image;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.models.Post;
import com.projects.socialmediaapi.user.payload.requests.PostRequest;
import com.projects.socialmediaapi.user.payload.responses.*;
import com.projects.socialmediaapi.user.repositories.ImageRepository;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import com.projects.socialmediaapi.user.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.projects.socialmediaapi.user.constants.PostConstants.*;
import static com.projects.socialmediaapi.user.services.ImageService.getFileName;
import static com.projects.socialmediaapi.user.services.UserInteractionService.UserNotFoundException;
import static com.projects.socialmediaapi.user.services.UserInteractionService.getTimestamp;

@Service
@RequiredArgsConstructor
public class PostService {

    // -----------------------------------------------------------------------------------------------------------------

    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    // CREATE ----------------------------------------------------------------------------------------------------------
    @Transactional
    public UploadPostResponse createPost(PostRequest request) {

        Person person = getAuthenticatePerson();

        if (imageRequestIsNull(request)) {
            return getUploadPostResponse(request, person);
        }

        Image image = imageService.getImage(request, person);

        return getUploadPostResponse(request,
                image,
                getFileDownloadUri(image));
    }

    // UPDATE ----------------------------------------------------------------------------------------------------------

    @Transactional
    public UpdatePostResponse updatePost(PostRequest request, Long id) throws IOException {
        Person person = getAuthenticatePerson();
        Post post = getPostById(id);

        checkIfPersonNotAuthor(person, post);

        setTitleBodyTimestamp(request, post);

        if (imageRequestIsNull(request)) {
            return saveWithoutImage(id, post);
        }

        imageUpdate(request, post);
        return getUpdatePostResponse(id, post);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private UpdatePostResponse saveWithoutImage(Long id, Post post) {
        postRepository.save(post);
        return getUpdatePostResponse(id, post);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void setTitleBodyTimestamp(PostRequest request, Post post) {
        post.setTitle(request.getTitle());
        post.setBody(request.getBody());
        post.setTimestamp(getTimestamp(LocalDateTime.now()));
    }

    // DELETE ----------------------------------------------------------------------------------------------------------

    @Transactional
    public DeletePostResponse deletePost(Long id) {
        Person person = getAuthenticatePerson();

        Post post = getPostById(id);

        checkIfPersonNotAuthor(person, post);

        postRepository.delete(post);
        return getDeletePostResponse(id);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void checkIfPersonNotAuthor(Person person, Post post) {
        if (isPersonNotAuthor(person, post)) {
            throw new UnauthorizedPostAction(UNAUTHORIZED_POST_ACTION);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean isPersonNotAuthor(Person person, Post post) {
        return !Objects.equals(person.getId(), post.getPerson().getId());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Post getPostById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static DeletePostResponse getDeletePostResponse(Long id) {
        return DeletePostResponse.builder()
                .id(id)
                .message(String.format(POST_DELETED, id))
                .build();
    }

    // SHOW ------------------------------------------------------------------------------------------------------------

    @Transactional
    public PostResponse showAllPostsByUserId(Long postId) {
        Person person = getPersonById(postId);
        return getPostResponse(person);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static PostResponse getPostResponse(Person person) {
        return PostResponse.builder()
                .posts(person.getPosts())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Person getPersonById(Long postId) {
        return personRepository.findById(postId)
                .orElseThrow(UserNotFoundException());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private boolean imageRequestIsNull(PostRequest request) {
        return request.getImage() == null || request.getImage().isEmpty();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private UploadPostResponse getUploadPostResponse(PostRequest request, Person person) {
        postRepository.save(createPostWithoutImage(request, person));
        return UploadPostResponse.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .message(CREATE_SUCCESS)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Post createPostWithoutImage(PostRequest request, Person person) {
        return Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .person(person)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Person getAuthenticatePerson() {
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return getPersonByEmail(personDetails);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static String getFileDownloadUri(Image image) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(image.getId()))
                .toUriString();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static UploadPostResponse getUploadPostResponse(PostRequest request,
                                                            Image image,
                                                            String fileDownloadUri) {
        return UploadPostResponse.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .imageResponse(getImageResponse(request, image, fileDownloadUri))
                .message(CREATE_SUCCESS)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static UpdatePostResponse getUpdatePostResponse(Long id, Post post) {
        return UpdatePostResponse.builder()
                .id(post.getId())
                .message(String.format(POST_UPDATED, id))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void imageUpdate(PostRequest request, Post post) throws IOException {
        Image image = getImageByPostId(post);
        setAllPropertiesImage(request, image);
        savePostAndImage(post, image);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void setAllPropertiesImage(PostRequest request, Image image) throws IOException {
        image.setData(request.getImage().getBytes());
        image.setFileName(getFileName(request.getImage()));
        image.setFileType(request.getImage().getContentType());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Image getImageByPostId(Post post) {
        return imageRepository
                .findById(post
                        .getImage()
                        .getId())
                .orElseThrow(() -> new ImageNotFoundException(IMAGE_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void savePostAndImage(Post post, Image image) {
        postRepository.save(post);
        imageRepository.save(image);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Person getPersonByEmail(PersonDetails personDetails) {
        return personRepository.findByEmail(personDetails.getUsername())
                .orElseThrow(UserNotFoundException());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static ImageResponse getImageResponse(PostRequest request,
                                                  Image image,
                                                  String fileDownloadUri) {
        return ImageResponse.builder()
                .fileName(image.getFileName())
                .contentType(image.getFileType())
                .downloadUri(fileDownloadUri)
                .size(request.getImage().getSize())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
}
