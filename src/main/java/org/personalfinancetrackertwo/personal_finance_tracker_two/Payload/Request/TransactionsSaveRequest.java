package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.personalfinancetrackertwo.personal_finance_tracker_two.DTO.TransactionDTO;

import java.util.ArrayList;

public class TransactionsSaveRequest {

    @NotEmpty(message = "transactions list cannot be empty.")
    @Valid
    private ArrayList<TransactionDTO> transactions;

    public TransactionsSaveRequest(ArrayList<TransactionDTO> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
