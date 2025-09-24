package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response;

public class AuthUserResponse {

    private String authUser;

    public AuthUserResponse(String authUser) {
        this.authUser = authUser;
    }

    AuthUserResponse() {

    }

    public String getAuthUser() {
        return authUser;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

}
