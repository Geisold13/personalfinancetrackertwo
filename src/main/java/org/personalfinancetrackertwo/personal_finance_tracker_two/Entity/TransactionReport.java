package org.personalfinancetrackertwo.personal_finance_tracker_two.Entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transaction_report")
public class TransactionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_report_id", nullable = false)
    private long transactionReportId;

    @Column(name = "transaction_report_name", nullable = false)
    private String transactionReportName;

    @Column(name = "transaction_report_description")
    private String transactionReportDescription;

    @Column(name = "transaction_report_creation_date", nullable = false)
    private Timestamp transactionReportCreationDate;

    @Column(name = "transaction_report_last_saved")
    private Timestamp transactionReportLastSaved;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User transactionReportUser;

    @OneToMany(mappedBy = "transactionReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportTransaction> reportTransactions;

    public TransactionReport(long transactionReportId, String transactionReportName, String transactionReportDescription, Timestamp transactionReportCreationDate, Timestamp transactionReportLastSaved, User transactionReportUser, List<ReportTransaction> reportTransactions) {
        this.transactionReportId = transactionReportId;
        this.transactionReportName = transactionReportName;
        this.transactionReportDescription = transactionReportDescription;
        this.transactionReportCreationDate = transactionReportCreationDate;
        this.transactionReportLastSaved = transactionReportLastSaved;
        this.transactionReportUser = transactionReportUser;
        this.reportTransactions = reportTransactions;

    }

    public TransactionReport() {}

    public long transactionReportId() {
        return transactionReportId;
    }

    public void setTransactionReportId(long transactionReportId) {
        this.transactionReportId = transactionReportId;
    }

    public String getTransactionReportName() {
        return this.transactionReportName;
    }

    public void setTransactionReportName(String transactionReportName) {
        this.transactionReportName = transactionReportName;
    }

    public String getTransactionReportDescription() {
        return this.transactionReportDescription;
    }

    public void setTransactionReportDescription(String transactionReportDescription) {
        this.transactionReportDescription = transactionReportDescription;
    }

    public Timestamp getTransactionReportCreationDate() {
        return this.transactionReportCreationDate;
    }

    public void setTransactionReportCreationDate(Timestamp transactionReportCreationDate) {
        this.transactionReportCreationDate = transactionReportCreationDate;
    }

    public Timestamp getTransactionReportLastSaved() {
        return this.transactionReportLastSaved;
    }

    public void setTransactionReportLastSaved(Timestamp transactionReportLastSaved) {
        this.transactionReportLastSaved = transactionReportLastSaved;
    }

    public User getTransactionReportUser() {
        return this.transactionReportUser;
    }

    public void setTransactionReportUser(User transactionReportUser) {
        this.transactionReportUser = transactionReportUser;
    }

    public List<ReportTransaction> getReportTransactions() {
        return reportTransactions;
    }

    public void setReportTransactions(List<ReportTransaction> reportTransactions) {
        this.reportTransactions = reportTransactions;
    }
}
