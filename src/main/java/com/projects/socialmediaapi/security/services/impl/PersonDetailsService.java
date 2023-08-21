package com.projects.socialmediaapi.security.services.impl;

import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import com.projects.socialmediaapi.user.services.UserInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.projects.socialmediaapi.user.constants.UserConstants.USER_NOT_FOUND;
import static com.projects.socialmediaapi.user.services.UserInteractionService.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    // -----------------------------------------------------------------------------------------------------------------

    private final PersonRepository personRepository;

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository
                .findByEmail(username)
                .orElseThrow(UserNotFoundException());

        return new PersonDetails(person);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
