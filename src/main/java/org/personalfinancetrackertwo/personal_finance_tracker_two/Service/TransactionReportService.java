package org.personalfinancetrackertwo.personal_finance_tracker_two.Service;


import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.TransactionReportSaveRequest;

public interface TransactionReportService {

    void saveTransactionReport(TransactionReportSaveRequest request);
}
