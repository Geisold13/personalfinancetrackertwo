package org.personalfinancetrackertwo.personal_finance_tracker_two.Controller;

import jakarta.validation.Valid;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.TransactionsSaveRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.GetTransactionsResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.RegistrationResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.TransactionsSaveResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("/transaction/savetransactions")
    public ResponseEntity<TransactionsSaveResponse> saveTransactions(@Valid @RequestBody TransactionsSaveRequest request) {
        TransactionsSaveResponse response = new TransactionsSaveResponse(transactionService.saveTransactions(request), "Transactions saved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/transaction/gettransactions")
    public ResponseEntity<GetTransactionsResponse> getTransactions() {

        GetTransactionsResponse response = new GetTransactionsResponse(transactionService.getTransactions(), "Transactions retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
