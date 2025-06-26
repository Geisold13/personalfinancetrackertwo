package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request;


import jakarta.validation.constraints.*;

public class AuthenticationRequest {

    @Size(min = 6, max = 256, message = "Email needs to be between 6 and 256 characters.")
    @NotBlank(message = "Email is required. Cannot be blank or whitespace only.")
    @Email(message = "Invalid email format.")
    private String email;

    @Size(min = 8, max = 256, message = "Password needs to be between 8 and 256 characters.")
    @NotEmpty(message = "Password is required. Cannot be blank or whitespace only.")
    @Pattern(regexp = "^\\S.*\\S$|^\\S$", message = "No leading or trailing whitespace allowed.")
    private String password;


    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;

    }

    public AuthenticationRequest() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
