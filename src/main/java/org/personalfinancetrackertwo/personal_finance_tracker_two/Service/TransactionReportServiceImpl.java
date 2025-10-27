package org.personalfinancetrackertwo.personal_finance_tracker_two.Service;

import org.personalfinancetrackertwo.personal_finance_tracker_two.DTO.TransactionDTO;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.ReportTransaction;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.TransactionReport;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.TransactionType;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.TransactionReportSaveRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Repository.ReportTransactionRepository;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Repository.TransactionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class TransactionReportServiceImpl implements TransactionReportService {

    private final TransactionReportRepository transactionReportRepository;
    private final UserService userService;
    private final ReportTransactionRepository reportTransactionRepository;

    @Autowired
    public TransactionReportServiceImpl(TransactionReportRepository transactionReportRepository, UserService userService, ReportTransactionRepository reportTransactionRepository) {
        this.transactionReportRepository = transactionReportRepository;
        this.userService = userService;
        this.reportTransactionRepository = reportTransactionRepository;
    }

    public void saveTransactionReport(TransactionReportSaveRequest request) {
        ArrayList<TransactionDTO> transactions = request.getTransactions();
        String transactionName = request.getTransactionReportName();
        String transactionDescription = request.getTransactionReportDescription();

        TransactionReport newTransactionReport = new TransactionReport();
        newTransactionReport.setTransactionReportName(transactionName);
        newTransactionReport.setTransactionReportDescription(transactionDescription);
        newTransactionReport.setTransactionReportCreationDate(new Timestamp(System.currentTimeMillis()));
        newTransactionReport.setTransactionReportLastSaved(new Timestamp(System.currentTimeMillis()));
        newTransactionReport.setTransactionReportUser(userService.getAuthenticatedUser());

        ArrayList<ReportTransaction> newReportTransactions = new ArrayList<>();

        for (TransactionDTO transactionDTO : transactions) {

            ReportTransaction newReportTransaction = new ReportTransaction();

            if (transactionDTO.getTransactionType().equals("Income")) {
                newReportTransaction.setReportTransactionType(TransactionType.INCOME);
            } else {
                newReportTransaction.setReportTransactionType(TransactionType.EXPENSE);
            }
            newReportTransaction.setReportTransactionName(transactionDTO.getTransactionName());
            newReportTransaction.setReportTransactionAmount(transactionDTO.getTransactionAmount());
            newReportTransaction.setReportTransactionDate(transactionDTO.getTransactionDate());
            newReportTransaction.setReportTransactionCategory(transactionDTO.getTransactionCategory());
            newReportTransaction.setReportTransactionDescription(transactionDTO.getTransactionDescription());
            newReportTransaction.setReportTransactionInitialSave(new Timestamp(System.currentTimeMillis()));
            newReportTransaction.setReportTransactionLastSaved(new Timestamp(System.currentTimeMillis()));

            newReportTransaction.setTransactionReport(newTransactionReport);
            newReportTransactions.add(newReportTransaction);
        }

        newTransactionReport.setReportTransactions(newReportTransactions);
        transactionReportRepository.save(newTransactionReport);
    }
}
