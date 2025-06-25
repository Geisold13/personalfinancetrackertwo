package org.personalfinancetrackertwo.personal_finance_tracker_two.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter is a Filter that intercepts all incoming HTTP Requests,
 * checks for a Json Web Token (JWT) in the Authorization header, checks for token validation,
 * and then authenticates the user by setting the Authentication object in the Spring Security context.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Declares jwtUtil and UserDetailsService dependencies
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // constructor injection of the dependencies
    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method extracts the Json Web Token from the incoming HTTP request Authorization Header (if possible), checks its validity,
     * if it's valid and not expired, then it authenticates the user within the Spring Security Context.  Then moves onto the next filter.
     * @param request - incoming HTTP request
     * @param response - HTTP response
     * @param filterChain - Chain of filters the request passes through
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // extracts the authorization header from the incoming HTTP request
        final String authorizationHeader = request.getHeader("Authorization");

        // declares userEmail and jwt Strings
        final String userEmail;
        String jwt;

        // conditional checks if the authorizationHeader doesn't exist or if it doesn't start with "Bearer"
        // if so, passes the incoming HTTP request and the response to the next filter and then returns from the method.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // extracts the Jwt from the authorizationHeader and the userEmail from the Jwt
        jwt = authorizationHeader.substring(7);
        userEmail = jwtUtil.extractUsername(jwt);

        // checks if the userEmail exists and that there isn't a user authenticated in the spring security context
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // fetches userDetails using the userEmail, from the userDetailsService using the loadUserByUsername method
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // conditional checks if the jwt is valid, if so, authenticates the user
            if (jwtUtil.isTokenValid(jwt, userDetails)) {

                // creates a new UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // sets details for the token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // sets the authToken in the SecurityContextHolder, so Spring Security now recognizes the user as authenticated.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // once authenticated, moves the HTTP request and response to the next filter in the filter chain
        filterChain.doFilter(request, response);
    }
}
