package org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.personalfinancetrackertwo.personal_finance_tracker_two.DTO.TransactionDTO;

import java.util.ArrayList;

public class TransactionReportSaveRequest {

    @NotBlank(message = "Transaction Name is required.")
    private String transactionReportName;

    @NotNull(message = "Report Description cannot be null.")
    private String transactionReportDescription;

    @NotEmpty(message = "Transactions list cannot be empty.")
    @Valid
    private ArrayList<TransactionDTO> transactions;

    public TransactionReportSaveRequest(String transactionReportName, String transactionReportDescription, ArrayList<TransactionDTO> transactions) {
        this.transactionReportName = transactionReportName;
        this.transactionReportDescription = transactionReportDescription;
        this.transactions = transactions;

    }

    public TransactionReportSaveRequest() {}

    public String getTransactionReportName() {
        return transactionReportName;
    }

    public void setTransactionReportName(String transactionReportName) {
        this.transactionReportName = transactionReportName;
    }

    public String getTransactionReportDescription() {
        return transactionReportDescription;
    }

    public void setTransactionReportDescription(String transactionReportDescription) {
        this.transactionReportDescription = transactionReportDescription;
    }

    public ArrayList<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
