package org.personalfinancetrackertwo.personal_finance_tracker_two.Entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable=false)
    private long transactionId;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "transaction_name", nullable = false)
    private String transactionName;

    @Column(name = "transaction_amount", nullable = false)
    private double transactionAmount;

    @Column(name = "transaction_date", nullable = false)
    private String transactionDate;

    @Column(name = "transaction_category", nullable = false)
    private String transactionCategory;

    @Column(name = "transaction_description", nullable = false)
    private String transactionDescription;

    @Column(name = "transaction_initial_save", nullable = false)
    private Timestamp transactionInitialSave;

    @Column(name = "transaction_last_saved", nullable = false)
    private Timestamp transactionLastSaved;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User transactionUser;

    public Transaction(long transactionId, TransactionType transactionType, String transactionName, double transactionAmount, String transactionDate, String transactionCategory, String transactionDescription, Timestamp transactionInitialSave, Timestamp transactionLastSaved, User transactionUser) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.transactionName = transactionName;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionCategory = transactionCategory;
        this.transactionDescription = transactionDescription;
        this.transactionInitialSave = transactionInitialSave;
        this.transactionLastSaved = transactionLastSaved;
        this.transactionUser = transactionUser;
    }

    public Transaction() {}

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
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

    public Timestamp getTransactionInitialSave() {
        return transactionInitialSave;
    }

    public void setTransactionInitialSave(Timestamp transactionInitialSave) {
        this.transactionInitialSave = transactionInitialSave;
    }

    public Timestamp getTransactionLastSaved() {
        return transactionLastSaved;
    }

    public void setTransactionLastSaved(Timestamp transactionLastSaved) {
        this.transactionLastSaved = transactionLastSaved;
    }

    public User getTransactionUser() {
        return transactionUser;
    }

    public void setTransactionUser(User transactionUser) {
        this.transactionUser = transactionUser;
    }
}
