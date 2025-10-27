package org.personalfinancetrackertwo.personal_finance_tracker_two.Controller;

import jakarta.validation.Valid;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.TransactionReport;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.TransactionReportSaveRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.TransactionReportSaveResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Service.TransactionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionReportController {

    private final TransactionReportService transactionReportService;

    @Autowired
    public TransactionReportController(TransactionReportService transactionReportService) {
        this.transactionReportService = transactionReportService;
    }

    @PostMapping("/transactionreport/savetransactionreport")
    public ResponseEntity<TransactionReportSaveResponse> saveTransactionReport(@Valid @RequestBody TransactionReportSaveRequest request) {
        transactionReportService.saveTransactionReport(request);
        TransactionReportSaveResponse response = new TransactionReportSaveResponse("Transaction Report Saved Successfully!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
