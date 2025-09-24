package org.personalfinancetrackertwo.personal_finance_tracker_two.Service;

import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.User;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User getAuthenticatedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return userRepository.findByUserEmail(username).orElseThrow();

    }
}
