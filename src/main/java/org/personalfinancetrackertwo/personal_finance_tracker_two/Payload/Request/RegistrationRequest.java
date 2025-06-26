package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request;


import jakarta.validation.constraints.*;

public class RegistrationRequest {

    @Size(min = 2, max = 50, message = "First name needs to be between 2 and 50 characters.")
    @NotBlank(message = "First name is required. Cannot be blank or whitespace only.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must only contain letters.")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name needs to be between 2 and 50 characters.")
    @NotBlank(message = "Last name is required. Cannot be blank or whitespace only.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must only contain letters.")
    private String lastName;

    @Size(min = 6, max = 256, message = "Email needs to be between 6 and 256 characters.")
    @NotBlank(message = "Email is required. Cannot be blank or whitespace only.")
    @Email(message = "Invalid email format.")
    private String email;

    @Size(min = 8, max = 256, message = "Password needs to be between 8 and 256 characters.")
    @NotEmpty(message = "Password is required. Cannot be blank or whitespace only.")
    @Pattern(regexp = "^\\S.*\\S$|^\\S$", message = "No leading or trailing whitespace allowed.")
    private String password;

    public RegistrationRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public RegistrationRequest() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
