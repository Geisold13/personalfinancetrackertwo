package org.personalfinancetrackertwo.personal_finance_tracker_two.Repository;

import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail(String email);

    boolean existsByUserEmail(String email);
}
