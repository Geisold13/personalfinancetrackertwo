package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response;

public class AuthenticationResponse {

    private String token;
    private String message;


    public AuthenticationResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public AuthenticationResponse() {

    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
