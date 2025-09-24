package org.personalfinancetrackertwo.personal_finance_tracker_two.Mapper;

import org.personalfinancetrackertwo.personal_finance_tracker_two.DTO.TransactionDTO;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.Transaction;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.TransactionType;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionMapper {

    public ArrayList<TransactionDTO> convertToTransactionDTO(ArrayList<Transaction> transactions) {

        ArrayList<TransactionDTO> transactionDTOs = new ArrayList<>();
        for (Transaction transaction : transactions) {

            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransactionID(transaction.getTransactionId());

            if (transaction.getTransactionType().equals(TransactionType.INCOME)) {
                transactionDTO.setTransactionType("Income");
            } else if (transaction.getTransactionType().equals(TransactionType.EXPENSE)) {
                transactionDTO.setTransactionType("Expense");
            } else {
                throw new IllegalArgumentException("Invalid transaction type");
            }

            transactionDTO.setTransactionName(transaction.getTransactionName());
            transactionDTO.setTransactionAmount(transaction.getTransactionAmount());
            transactionDTO.setTransactionDate(transaction.getTransactionDate());
            transactionDTO.setTransactionCategory(transaction.getTransactionCategory());
            transactionDTO.setTransactionDescription(transaction.getTransactionDescription());
            transactionDTOs.add(transactionDTO);
        }

        return transactionDTOs;
    }
}
