package com.projects.socialmediaapi.security.jwt;

import com.projects.socialmediaapi.security.jwt.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.projects.socialmediaapi.security.constants.AuthConstants.AUTHORIZATION;
import static com.projects.socialmediaapi.security.constants.AuthConstants.BEARER;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final Optional<String> jwt = parseJwt(request);

        jwt.ifPresent(token -> {
            try {
                String userEmail = jwtUtils.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtUtils.isTokenValid(token, userDetails)) {
                    final UsernamePasswordAuthenticationToken authToken = getAuthToken(userDetails);
                    setAuthTokenDetails(request, authToken);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (ExpiredJwtException e) {
                request.setAttribute("expired", e.getMessage());
            } catch (SignatureException e) {
                request.setAttribute("incorrect", e.getMessage());
            }
        });
        filterChain.doFilter(request, response);
    }

    private static void setAuthTokenDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authToken) {
        authToken.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request));
    }

    private static UsernamePasswordAuthenticationToken getAuthToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    private static Optional<String> parseJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER)) {
            return Optional.of(authHeader.substring(7));
        }

        return Optional.empty();
    }
}
