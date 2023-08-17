package com.projects.socialmediaapi.utils.mappers;

import com.projects.socialmediaapi.post.models.Post;
import com.projects.socialmediaapi.post.payload.requests.PostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    Post toPost(PostRequest request);
}
