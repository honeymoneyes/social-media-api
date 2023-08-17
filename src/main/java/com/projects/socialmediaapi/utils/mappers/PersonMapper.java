package com.projects.socialmediaapi.utils.mappers;

import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.user.models.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonMapper {
    @Mapping(target = "id", ignore = true)
    Person toPerson(RegisterRequest request);
}
