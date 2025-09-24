package org.personalfinancetrackertwo.personal_finance_tracker_two.Repository;

import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.Transaction;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    ArrayList<Transaction> getAllByTransactionUser(User transactionUser);

    boolean existsByTransactionId(Long transactionId);

    Transaction getTransactionByTransactionId(Long transactionId);
}
