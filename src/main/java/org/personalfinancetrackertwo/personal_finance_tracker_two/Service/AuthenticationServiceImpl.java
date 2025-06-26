package org.personalfinancetrackertwo.personal_finance_tracker_two.Service;

import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.Role;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.User;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Exception.AuthenticationException;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Exception.EmailAlreadyExistsException;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.AuthenticationRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.RegistrationRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Repository.UserRepository;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * AuthenticationServiceImpl is responsible for handling account creation and sign in authentication logic
 * This includes throwing custom exceptions, creating new Users, updating Users and authenticating Users
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    // dependencies declared
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // dependencies are injected via constructor
    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;

    }

    /**
     * This method is responsible for registering a new user.
     * @param registrationRequest - registration data from the prospective user.
     */
    @Override
    public void register(RegistrationRequest registrationRequest) {

        // conditional checks if a user already exists with given email, if so, throws an exception
        if (userRepository.existsByUserEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");

        } else {
            // otherwise, creates a new User entity and sets its values to the values within the registrationRequest
            // along with the role and creation date.
            User newUser = new User();
            newUser.setUserFirstName(registrationRequest.getFirstName());
            newUser.setUserLastName(registrationRequest.getLastName());
            newUser.setUserEmail(registrationRequest.getEmail());
            newUser.setUserPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            newUser.setUserRole(Role.USER);
            newUser.setUserCreationDate(new Timestamp(System.currentTimeMillis()));

            // saves the new user
            userRepository.save(newUser);
        }

    }

    /**
     * This method is responsible for authenticating a user sign in request.
     * @param authenticationRequest - User sign in information
     * @return
     */
    @Override
    public User authenticate(AuthenticationRequest authenticationRequest) {

        // fetches the user from the repository by email if it exists, otherwise throws an exception
        User authUser = userRepository.findByUserEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new AuthenticationException("User not found."));

        // tries to authenticate the user, if not, catches a BadCredentialsException and throws a custom exception
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authUser.getUserEmail(), authUser.getPassword()));
        } catch (BadCredentialsException e) {
           throw new AuthenticationException("Invalid username or password.");
        }

        // if user exists and credentials are valid, then the last sign in is updated, and the user is saved
        authUser.setUserLastSignIn(new Timestamp(System.currentTimeMillis()));
        userRepository.save(authUser);

        // user is returned from the method
        return authUser;
    }
}
