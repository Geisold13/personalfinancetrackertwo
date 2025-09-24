package org.personalfinancetrackertwo.personal_finance_tracker_two.Service;

import org.personalfinancetrackertwo.personal_finance_tracker_two.DTO.TransactionDTO;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.TransactionsSaveRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.GetTransactionsResponse;

import java.util.ArrayList;

public interface TransactionService {

    ArrayList<TransactionDTO> saveTransactions(TransactionsSaveRequest transactionsSaveRequest);

    ArrayList<TransactionDTO> getTransactions();
}
