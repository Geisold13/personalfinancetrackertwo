package org.personalfinancetrackertwo.personal_finance_tracker_two.Controller;

import jakarta.validation.Valid;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.User;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.AuthenticationRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.RegistrationRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.AuthUserResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.AuthenticationResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.RegistrationResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Service.AuthenticationService;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Service.UserService;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * AuthenticationController is responsible for handling HTTP requests to the /api/register and /api/authenticate
 * endpoints.  Each have individual http handler methods to handle their own respective incoming http requests
 */
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    // dependencies are declared
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // dependencies are injected via constructor
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * This http handler method is responsible for handling HTTP requests to the /api/register endpoint.
     * @param registrationRequest - incoming registration data.
     * @return - ResponseEntity with a registration response and http status.
     */
    @PostMapping("/auth/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest) {

        // registers new user and then returns appropriate response entity with success message and http response
       authenticationService.register(registrationRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrationResponse("User registered successfully!"));
    }

    /**
     * This http handler method is responsible for handling HTTP requests to the /api/authenticate endpoint.
     * @param authenticationRequest - incoming sign in data.
     * @return - ResponseEntity with an authentication response that has a jwt and message.
     */
    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        // grabs authenticated user and creates a jwt with that user
        User authUser = authenticationService.authenticate(authenticationRequest);
        String jwt = jwtUtil.generateJwt(authUser);
        String userFirstLast = authUser.getUserFirstName() + " " + authUser.getUserLastName();

        // returns appropriate ResponseEntity with success message and http status
        return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponse(jwt, userFirstLast, "User Logged in Successfully!"));

    }

    @GetMapping("/auth/user")
    public ResponseEntity<AuthUserResponse> getAuthUserName() {

        User authUser = userService.getAuthenticatedUser();
        String firstLast = authUser.getUserFirstName() + " " + authUser.getUserLastName();
        return ResponseEntity.status(HttpStatus.OK).body(new AuthUserResponse(firstLast));
    }
}
