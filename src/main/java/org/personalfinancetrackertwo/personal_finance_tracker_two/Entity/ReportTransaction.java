package org.personalfinancetrackertwo.personal_finance_tracker_two.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "report_transactions")
public class ReportTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_transaction_id", nullable = false)
    private long reportTransactionId;

    @Column(name = "report_transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType reportTransactionType;

    @Column(name = "report_transaction_name", nullable = false)
    private String reportTransactionName;

    @Column(name = "report_transaction_amount", nullable = false)
    private double reportTransactionAmount;

    @Column(name = "report_transaction_date", nullable = false)
    private String reportTransactionDate;

    @Column(name = "report_transaction_category", nullable = false)
    private String reportTransactionCategory;

    @Column(name = "report_transaction_description", nullable = false)
    private String reportTransactionDescription;

    @Column(name = "report_transaction_initial_save", nullable = false)
    private Timestamp reportTransactionInitialSave;

    @Column(name = "report_transaction_last_saved", nullable = false)
    private Timestamp reportTransactionLastSaved;
    
    @ManyToOne
    @JoinColumn(name = "transaction_report_id", referencedColumnName = "transaction_report_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TransactionReport transactionReport;

    public ReportTransaction(long reportTransactionId, TransactionType reportTransactionType, String reportTransactionName, double reportTransactionAmount, String reportTransactionDate, String reportTransactionCategory, String reportTransactionDescription, Timestamp reportTransactionInitialSave, Timestamp reportTransactionLastSaved, TransactionReport transactionReport) {
        this.reportTransactionId = reportTransactionId;
        this.reportTransactionType = reportTransactionType;
        this.reportTransactionName = reportTransactionName;
        this.reportTransactionAmount = reportTransactionAmount;
        this.reportTransactionDate = reportTransactionDate;
        this.reportTransactionCategory = reportTransactionCategory;
        this.reportTransactionDescription = reportTransactionDescription;
        this.reportTransactionInitialSave = reportTransactionInitialSave;
        this.reportTransactionLastSaved = reportTransactionLastSaved;
        this.transactionReport = transactionReport;
    }

    public ReportTransaction() {

    }

    public long getReportTransactionId() {
        return reportTransactionId;
    }

    public void setReportTransactionId(long reportTransactionId) {
        this.reportTransactionId = reportTransactionId;
    }

    public TransactionType getReportTransactionType() {
        return this.reportTransactionType;
    }

    public void setReportTransactionType(TransactionType reportTransactionType) {
        this.reportTransactionType = reportTransactionType;
    }

    public String getReportTransactionName() {
        return this.reportTransactionName;
    }

    public void setReportTransactionName(String reportTransactionName) {
        this.reportTransactionName = reportTransactionName;
    }

    public double getReportTransactionAmount() {
        return this.reportTransactionAmount;
    }

    public void setReportTransactionAmount(double reportTransactionAmount) {
        this.reportTransactionAmount = reportTransactionAmount;
    }

    public String getReportTransactionDate() {
        return this.reportTransactionDate;
    }

    public void setReportTransactionDate(String reportTransactionDate) {
        this.reportTransactionDate = reportTransactionDate;
    }

    public String getReportTransactionCategory() {
        return this.reportTransactionCategory;
    }

    public void setReportTransactionCategory(String reportTransactionCategory) {
        this.reportTransactionCategory = reportTransactionCategory;
    }

    public String getReportTransactionDescription() {
        return this.reportTransactionDescription;
    }

    public void setReportTransactionDescription(String reportTransactionDescription) {
        this.reportTransactionDescription = reportTransactionDescription;
    }

    public Timestamp getReportTransactionInitialSave() {
        return this.reportTransactionInitialSave;
    }

    public void setReportTransactionInitialSave(Timestamp reportTransactionInitialSave) {
        this.reportTransactionInitialSave = reportTransactionInitialSave;
    }

    public Timestamp getReportTransactionLastSaved() {
        return this.reportTransactionLastSaved;
    }

    public void setReportTransactionLastSaved(Timestamp reportTransactionLastSaved) {
        this.reportTransactionLastSaved = reportTransactionLastSaved;
    }

    public TransactionReport transactionReport() {
        return transactionReport;
    }

    public void setTransactionReport(TransactionReport transactionReport) {
        this.transactionReport = transactionReport;
    }
}
