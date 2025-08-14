package org.personalfinancetrackertwo.personal_finance_tracker_two;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Exception.CustomAuthenticationException;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Exception.EmailAlreadyExistsException;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.User;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.AuthenticationRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.RegistrationRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.RegistrationResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Repository.UserRepository;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Service.AuthenticationService;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Service.AuthenticationServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;


    @Test
    void register_shouldThrow_whenEmailAlreadyExists() {

        // arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "johndoe@email.com";
        String password = "password";

        RegistrationRequest registrationRequest = new RegistrationRequest(firstName, lastName, email, password);
        when(userRepository.existsByUserEmail(registrationRequest.getEmail())).thenReturn(true);

        // act and assert
        assertThrows(EmailAlreadyExistsException.class, () -> authenticationService.register(registrationRequest));

        // assert
        verify(userRepository).existsByUserEmail(registrationRequest.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldSave_whenEmailDoesNotExist() {

        // arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "johndoe@email.com";
        String password = "password";

        RegistrationRequest registrationRequest = new RegistrationRequest(firstName, lastName, email, password);
        when(userRepository.existsByUserEmail(email)).thenReturn(false);

        // act
        authenticationService.register(registrationRequest);

        // assert
        verify(userRepository).existsByUserEmail(registrationRequest.getEmail());
        verify(userRepository).save(any());
    }

    @Test
    void authenticate_shouldThrow_whenUserNotFound() {

        // arrange
        String email = "johndoe@email.com";
        String password = "password";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);
        when(userRepository.findByUserEmail(email)).thenReturn(Optional.empty());

        // act
        CustomAuthenticationException ex = assertThrows(CustomAuthenticationException.class, () -> authenticationService.authenticate(authenticationRequest));

        // assert
        verify(userRepository).findByUserEmail(email);
        verify(userRepository, never()).save(any());
        assertThat(ex.getMessage()).isEqualTo("User not found.");
    }


    @Test
    void authenticate_shouldThrow_whenPasswordDoesNotMatch() {

        // arrange
        String email = "johndoe@email.com";
        String password = "password";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        User mockUser = new User();
        mockUser.setUserEmail(email);
        mockUser.setUserPassword(password);

        when(userRepository.findByUserEmail(email)).thenReturn(Optional.of(mockUser));
        doThrow(new BadCredentialsException("Bad Credentials"))
                .when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // act
        CustomAuthenticationException ex = assertThrows(CustomAuthenticationException.class, () -> authenticationService.authenticate(authenticationRequest));

        // assert
        verify(userRepository).findByUserEmail(authenticationRequest.getEmail());
        verify(userRepository, never()).save(any());
        assertThat(ex.getMessage()).isEqualTo("Incorrect password.");
    }

    @Test
    void authenticate_shouldSaveAndReturnUser_whenCredentialsMatch() {

        // arrange
        String email = "johndoe@email.com";
        String password = "password";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        User mockUser = new User();
        mockUser.setUserEmail(email);
        mockUser.setUserPassword(password);

        when(userRepository.findByUserEmail(email)).thenReturn(Optional.of(mockUser));

        // creates a mock Authentication object representing valid credentials were entered successfully
        Authentication mockAuth = new UsernamePasswordAuthenticationToken(email, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        // simulates AuthenticationManager returning an Authentication object
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);

        // act
        User returnedUser = authenticationService.authenticate(authenticationRequest);

        // assert
        assertThat(returnedUser).isNotNull();
        assertThat(returnedUser.getUserEmail()).isEqualTo(email);
        assertThat(returnedUser.getUserPassword()).isEqualTo(password);

    }
}
