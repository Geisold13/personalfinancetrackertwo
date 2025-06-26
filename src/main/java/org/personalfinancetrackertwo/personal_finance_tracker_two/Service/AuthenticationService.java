package org.personalfinancetrackertwo.personal_finance_tracker_two.Service;

import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.User;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.AuthenticationRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.RegistrationRequest;

public interface AuthenticationService {

    void register(RegistrationRequest registrationRequest);

    User authenticate(AuthenticationRequest authenticationRequest);
}
