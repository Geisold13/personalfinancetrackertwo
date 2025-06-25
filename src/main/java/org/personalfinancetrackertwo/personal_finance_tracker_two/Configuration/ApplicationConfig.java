package org.personalfinancetrackertwo.personal_finance_tracker_two.Configuration;


import org.personalfinancetrackertwo.personal_finance_tracker_two.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *  The ApplicationConfig class is a Spring configuration class that
 *  contains bean definitions and configuration settings for the Spring application context.
 *  It is used to configure Spring components such as authentication, user details service, and password encoding
 */
@Configuration
public class ApplicationConfig {

    // UserRepository private field is declared to help access user data in the repository
    private final UserRepository userRepository;

    // Constructor injection of the UserRepository
    @Autowired
    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method finds a user by their email using the .findByUserEmail method provided by the userRepository
     * @return - UserDetailsService Bean, which is used by Spring Security to retrieve user specific data
     */
    @Bean
    public UserDetailsService userDetailsService() {

        // returns UserDetails associated with the username (email) or throws an exception
        return username -> userRepository.findByUserEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * This method defines a bean that will return AuthenticationProvider object which will be responsible
     * for authenticating a user during login
     * @return DaoAuthenticationProvider instance called authProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Creates a new instance of DaoAuthenticationProvider
        authProvider.setUserDetailsService(userDetailsService()); // injects authProvider with the userDetailsService to be used to retrieve user information
        authProvider.setPasswordEncoder(passwordEncoder()); // injects passwordEncoder to be used be the authProvider
        return authProvider;

    }

    /**
     * This method defines a bean for AuthenticationManager, where AuthenticationManager is responsible for authenticating users.
     * @param config - is an AuthenticationConfiguration class which provides configuration for authentication
     * @return - the default AuthenticationManager from Spring Security's configuration
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // returns the default AuthenticationManager from Spring Security's configuration
        return config.getAuthenticationManager();
    }

    /**
     * This method defines a bean for PasswordEncoder, which is responsible for encoding and validating passwords securely.
     * @return - Returns BCryptPasswordEncoder implementation of PasswordEncoder interface
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

