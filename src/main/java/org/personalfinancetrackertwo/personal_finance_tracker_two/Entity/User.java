package org.personalfinancetrackertwo.personal_finance_tracker_two.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User is a Spring Boot Entity that represents a real life user that uses
 * the Personal Finance Tracker Two application.  This entity is directly mapped
 * to a "users" table in the MySQL database.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", nullable = false)
    private long userId;

    @Column(name = "user_first_name", length = 50, nullable = false)
    private String userFirstName;

    @Column(name = "user_last_name", length = 50, nullable = false)
    private String userLastName;

    @Column(name = "user_email", length = 256, nullable = false)
    private String userEmail;

    @Column(name = "user_password", length = 256, nullable = false)
    private String userPassword;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @Column(name = "user_creation_date", nullable = false)
    private Timestamp userCreationDate;

    @Column(name = "user_last_sign_in")
    private Timestamp userLastSignIn;

    @Column(name = "user_last_sign_out")
    private Timestamp userLastSignOut;

    @OneToMany(mappedBy = "transactionUser")
    private List<Transaction> transactions;

    public User(long userId, String userFirstName, String userLastName, String userEmail, String userPassword, Role userRole, Timestamp userCreationDate, Timestamp userLastSignIn, Timestamp userLastSignOut, List<Transaction> transactions) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRole = userRole;
        this.userCreationDate = userCreationDate;
        this.userLastSignIn = userLastSignIn;
        this.userLastSignOut = userLastSignOut;
        this.transactions = transactions;
    }

    public User(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public User() {

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public Timestamp getUserCreationDate() {
        return userCreationDate;
    }

    public void setUserCreationDate(Timestamp userCreationDate) {
        this.userCreationDate = userCreationDate;

    }

    public Timestamp getUserLastSignIn() {
        return userLastSignIn;
    }

    public void setUserLastSignIn(Timestamp userLastSignIn) {
        this.userLastSignIn = userLastSignIn;
    }

    public Timestamp getUserLastSignOut() {
        return userLastSignOut;
    }

    public void setUserLastSignOut(Timestamp userLastSignOut) {
        this.userLastSignOut = userLastSignOut;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
