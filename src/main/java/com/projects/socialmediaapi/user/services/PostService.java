package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.exceptions.ImageNotFoundException;
import com.projects.socialmediaapi.user.exceptions.PostNotFoundException;
import com.projects.socialmediaapi.user.exceptions.UnauthorizedPostDeletedException;
import com.projects.socialmediaapi.user.exceptions.UnauthorizedPostUpdatedException;
import com.projects.socialmediaapi.user.models.Image;
import com.projects.socialmediaapi.user.models.Post;
import com.projects.socialmediaapi.user.payload.requests.PostRequest;
import com.projects.socialmediaapi.user.payload.responses.*;
import com.projects.socialmediaapi.user.repositories.ImageRepository;
import com.projects.socialmediaapi.user.repositories.PostRepository;
import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

import static com.projects.socialmediaapi.user.constants.PostConstants.*;
import static com.projects.socialmediaapi.user.services.ImageService.getFileName;
import static com.projects.socialmediaapi.user.constants.UserConstants.USER_NOT_FOUND;

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
    public UploadPostResponse createPost(PostRequest request) throws IOException {

        Person person = getAuthenticatePerson();

        Image image;
        if (imageRequestIsNull(request)) {
            return getUploadPostResponse(
                    request.getTitle(),
                    request.getBody());
        }

        image = imageService.createPostWithImageAndReturnImage(
                request,
                person);

        String fileDownloadUri = getFileDownloadUri(image);

        return getUploadPostResponse(request,
                image,
                fileDownloadUri);
    }


    // UPDATE ----------------------------------------------------------------------------------------------------------

    @Transactional
    public UpdatePostResponse updatePost(PostRequest request, Long id) throws IOException {
        Person person = getAuthenticatePerson();

        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND));

        if (!Objects.equals(person.getId(), post.getPerson().getId())) {
            throw new UnauthorizedPostUpdatedException(UNAUTHORIZED_POST_UPDATE);
        }

        post.setTitle(request.getTitle());
        post.setBody(request.getBody());

        if (imageRequestIsNull(request)) {
            postRepository.save(post);
            return getUpdatePostResponse(id, post);
        }

        imageUpdate(request, post);
        return getUpdatePostResponse(id, post);
    }

    // DELETE ----------------------------------------------------------------------------------------------------------

    @Transactional
    public DeletePostResponse deletePost(Long id) {
        Person person = getAuthenticatePerson();
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND));

        if (!Objects.equals(person.getId(), post.getPerson().getId())) {
            throw new UnauthorizedPostDeletedException(UNAUTHORIZED_POST_DELETE);
        }
        postRepository.delete(post);
        return DeletePostResponse.builder()
                .id(id)
                .message(String.format(POST_DELETED, id))
                .build();
    }

    // SHOW ------------------------------------------------------------------------------------------------------------

    @Transactional
    public PostResponse showAllPostsByUserId(Long postId) {
        Person person = personRepository.findById(postId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        return PostResponse.builder()
                .posts(person.getPosts())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private boolean imageRequestIsNull(PostRequest request) {
        return request.getImage() == null || request.getImage().isEmpty();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private UploadPostResponse getUploadPostResponse(String title, String body) {
        return UploadPostResponse.builder()
                .title(title)
                .body(body)
                .message(CREATE_SUCCESS)
                .build();
    }


    // -----------------------------------------------------------------------------------------------------------------


    private Person getAuthenticatePerson() {
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return personRepository.findByEmail(personDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
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
                .imageResponse(
                        ImageResponse.builder()
                                .fileName(image.getFileName())
                                .contentType(image.getFileType())
                                .downloadUri(fileDownloadUri)
                                .size(request.getImage().getSize())
                                .build())
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

        image.setData(request.getImage().getBytes());
        image.setFileName(getFileName(request.getImage()));
        image.setFileType(request.getImage().getContentType());

        postRepository.save(post);
        imageRepository.save(image);
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
}
