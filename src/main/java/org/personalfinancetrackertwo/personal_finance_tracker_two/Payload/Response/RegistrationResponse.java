package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response;

public class RegistrationResponse {

    private String message;

    public RegistrationResponse(String message) {
        this.message = message;
    }

    public RegistrationResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
