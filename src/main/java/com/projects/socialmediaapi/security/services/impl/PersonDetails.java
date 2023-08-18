package com.projects.socialmediaapi.security.services.impl;

import com.projects.socialmediaapi.user.models.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class PersonDetails implements UserDetails {

    // -----------------------------------------------------------------------------------------------------------------

    private Long id;
    private final Person person;

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Long getId() {
        return person.getId();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String getUsername() {
        return person.getEmail();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isEnabled() {
        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------
}
