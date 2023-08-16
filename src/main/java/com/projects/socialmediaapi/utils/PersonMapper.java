package com.projects.socialmediaapi.utils;

import com.projects.socialmediaapi.auth.payload.requests.LoginRequest;
import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.user.models.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonMapper {
    @Mapping(target = "id", ignore = true)
    Person toPerson(RegisterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    Person toPerson(LoginRequest request);
}
