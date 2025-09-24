package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response;

import org.personalfinancetrackertwo.personal_finance_tracker_two.DTO.TransactionDTO;

import java.util.ArrayList;

public class TransactionsSaveResponse {

    private ArrayList<TransactionDTO> transactions;
    private String message;

    public TransactionsSaveResponse(ArrayList<TransactionDTO> transactions, String message) {
        this.transactions = transactions;
        this.message = message;
    }

    public TransactionsSaveResponse() {

    }

    public ArrayList<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<TransactionDTO> transactions) {
        this.transactions = transactions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
