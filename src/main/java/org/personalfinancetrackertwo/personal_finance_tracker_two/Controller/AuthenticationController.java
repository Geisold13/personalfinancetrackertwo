package org.personalfinancetrackertwo.personal_finance_tracker_two.Controller;

import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.User;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.AuthenticationRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.RegistrationRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.AuthenticationResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.RegistrationResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Service.AuthenticationService;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthenticationController is responsible for handling HTTP requests to the /api/register and /api/authenticate
 * endpoints.  Each have individual http handler methods to handle their own respective incoming http requests
 */
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    // dependencies are declared
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    // dependencies are injected via constructor
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * This http handler method is responsible for handling HTTP requests to the /api/register endpoint.
     * @param registrationRequest - incoming registration data.
     * @return - ResponseEntity with a registration response and http status.
     */
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest) {

        // registers new user and then returns appropriate response entity with success message and http response
       authenticationService.register(registrationRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrationResponse("User registered successfully!"));
    }

    /**
     * This http handler method is responsible for handling HTTP requests to the /api/authenticate endpoint.
     * @param authenticationRequest - incoming sign in data.
     * @return - ResponseEntity with an authentication response that has a jwt and message.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {

        // grabs authenticated user and creates a jwt with that user
        User authUser = authenticationService.authenticate(authenticationRequest);
        String jwt = jwtUtil.generateJwt(authUser);

        // returns appropriate ResponseEntity with success message and http status
        return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponse(jwt, "User Logged in Successfully!"));

    }
}
