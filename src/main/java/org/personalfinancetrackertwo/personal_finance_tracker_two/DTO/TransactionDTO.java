package org.personalfinancetrackertwo.personal_finance_tracker_two.DTO;

import jakarta.validation.constraints.*;

public class TransactionDTO {

    @NotNull(message = "Transaction can't have a null id.")
    private long transactionID;

    @NotBlank(message = "Transaction Type is required for a transaction.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Transaction type must only contain letters.")
    private String transactionType;

    @NotBlank(message = "Transaction Name is required for a transaction.")
    @Size(min = 1, max = 100, message = "Transaction Name has be between 1 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Transaction name must only contain letters and spaces.")
    private String transactionName;

    @NotNull(message = "Transaction Amount is required for a transaction.")
    @Min(value = -1000000000, message = "Transaction Amount needs to be greater than -1,000,000,000." )
    @Max(value = 1000000000, message = "Transaction Amount needs to be less than 1,000,000,000.")
    private double transactionAmount;

    @NotBlank(message = "Transaction Date is required for a transaction.")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Transaction date needs to be in valid mm/dd/yyyy format.")
    private String transactionDate;

    @NotBlank(message = "Transaction Category is required for a transaction.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Transaction category must only contain letters and spaces.")
    private String transactionCategory;

    @NotBlank(message = "Transaction Description is required for a transaction.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Transaction description must only contain letters and spaces.")
    private String transactionDescription;

    public TransactionDTO(long transactionID, String transactionType, String transactionName, double transactionAmount, String transactionDate, String transactionCategory, String transactionDescription) {
        this.transactionID = transactionID;
        this.transactionType = transactionType;
        this.transactionName = transactionName;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionCategory = transactionCategory;
        this.transactionDescription = transactionDescription;
    }

    public TransactionDTO(String transactionType, String transactionName, double transactionAmount, String transactionDate, String transactionCategory, String transactionDescription) {
        this.transactionType = transactionType;
        this.transactionName = transactionName;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionCategory = transactionCategory;
        this.transactionDescription = transactionDescription;
    }

    public TransactionDTO() {}

    public long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }
}
