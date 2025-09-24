package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response;

public class AuthenticationResponse {

    private String token;
    private String user;
    private String message;


    public AuthenticationResponse(String token, String user, String message) {
        this.token = token;
        this.user = user;
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

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
