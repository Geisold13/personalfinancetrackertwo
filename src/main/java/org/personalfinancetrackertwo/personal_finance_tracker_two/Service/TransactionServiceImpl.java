package org.personalfinancetrackertwo.personal_finance_tracker_two.Service;

import org.personalfinancetrackertwo.personal_finance_tracker_two.DTO.TransactionDTO;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.Transaction;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.TransactionType;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.User;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Mapper.TransactionMapper;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Request.TransactionsSaveRequest;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Payload.Response.GetTransactionsResponse;
import org.personalfinancetrackertwo.personal_finance_tracker_two.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserService userService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.userService = userService;
    }

    /**
     * Saves transactions from the user into the db
     * @param transactionsSaveRequest contains transactions needed to be saved
     * @return ArrayList of Transactions(TransactionDTO's) back to the client
     */
    @Override
    public ArrayList<TransactionDTO> saveTransactions(TransactionsSaveRequest transactionsSaveRequest) {

        // grabs authenticated user
        User authUser = userService.getAuthenticatedUser();
        ArrayList<TransactionDTO> transactionsFromClient = transactionsSaveRequest.getTransactions();
        ArrayList<TransactionDTO> updatedTransactionsFromDb = new ArrayList<>();
        ArrayList<Transaction> transactionsFromDb = transactionRepository.getAllByTransactionUser(authUser);

        // if user has no transactions saved into the database, all transactions can be saved from the client side without having to check for updates or deletions
        if (transactionsFromDb.isEmpty()) {

            // iterates through each TransactionDTO from the client, creates a new Transaction from processing the TransactionDTO
            // saves new Transaction to db, sets id of the TransactionDTO and adds that TransactionDTO with its new Id to the updatedTransactionsFromDb ArrayList to
            // be sent back to the client side
            for (TransactionDTO transactionDTO : transactionsFromClient) {

                Transaction transactionToSave = processNewTransaction(transactionDTO);
                Transaction savedTransaction = transactionRepository.save(transactionToSave);
                transactionDTO.setTransactionID(savedTransaction.getTransactionId());
                updatedTransactionsFromDb.add(transactionDTO);
            }
        // if db isn't empty, transactions from client are processed differently, looking for edits of existing Transactions, new Transactions, and deletions of Transactions.
        } else {
            HashSet<Long> idsFromClient = new HashSet<>(); // stores Transaction id's from client.

            // iterates through TransactionDTO's from client
            for (TransactionDTO transactionDTO : transactionsFromClient) {
                // grabs id from DTO and adds to HashSet
                long id = transactionDTO.getTransactionID();
                idsFromClient.add(id);
                boolean doesTransactionExist = transactionRepository.existsByTransactionId(id);
                // checks if Transaction exists by id provided by the DTO, in the db
                // if so, processes existing transaction, saves the updated transaction, and adds the processed transactionDTO back to the updatedTransactionsFromDb ArrayList
                if (doesTransactionExist) {
                    Transaction existingTransaction = transactionRepository.getTransactionByTransactionId(id);
                    if (!areTransactionsEqual(transactionDTO, existingTransaction)) {
                        transactionRepository.save(processExistingTransaction(transactionDTO, existingTransaction));
                    }

                } else { // otherwise if transaction doesn't exist in db by id, its new, so the new transaction is processed, saved, etc.
                    Transaction transactionToSave = processNewTransaction(transactionDTO);
                    Transaction savedTransaction = transactionRepository.save(transactionToSave);
                    transactionDTO.setTransactionID(savedTransaction.getTransactionId());
                }
                updatedTransactionsFromDb.add(transactionDTO);
            }

            // this iterates through all transactions from the Db, checks if the transaction id exists in the
            // HashSet of TransactionDTO ids from the client, if not, that mean's that transaction will be deleted from the db.
            for(Transaction transaction : transactionsFromDb) {
                if (!idsFromClient.contains(transaction.getTransactionId())) {
                    transactionRepository.delete(transaction);
                }
            }
        }
        return updatedTransactionsFromDb;
        // want to include where current transactions are compared to transactions saved in database, if so throw custom exception saying transactions are already saved up to date.
    }

    /**
     * Returns all transactions associated with authenticated user from db
     * @return ArrayList of transactions(TransactionDTO's) back to the user for viewing
     */
    public ArrayList<TransactionDTO> getTransactions() {

        User authUser = userService.getAuthenticatedUser();
        ArrayList<Transaction> transactionsFromDb = transactionRepository.getAllByTransactionUser(authUser);
        return transactionMapper.convertToTransactionDTO(transactionsFromDb);
    }


    private Transaction processNewTransaction(TransactionDTO transactionDTO) {
        User authUser = userService.getAuthenticatedUser();

        Transaction transactionToSave = new Transaction();

        if (transactionDTO.getTransactionType().equals("Income")) {
            transactionToSave.setTransactionType(TransactionType.INCOME);
        } else if (transactionDTO.getTransactionType().equals("Expense")) {
            transactionToSave.setTransactionType(TransactionType.EXPENSE);
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        transactionToSave.setTransactionName(transactionDTO.getTransactionName());
        transactionToSave.setTransactionDate(transactionDTO.getTransactionDate());
        transactionToSave.setTransactionAmount(transactionDTO.getTransactionAmount());
        transactionToSave.setTransactionCategory(transactionDTO.getTransactionCategory());
        transactionToSave.setTransactionDescription(transactionDTO.getTransactionDescription());
        transactionToSave.setTransactionInitialSave(new Timestamp(System.currentTimeMillis()));
        transactionToSave.setTransactionLastSaved(new Timestamp(System.currentTimeMillis()));
        transactionToSave.setTransactionUser(authUser);

        return transactionToSave;
    }

    private Transaction processExistingTransaction(TransactionDTO transactionDTO, Transaction transactionToUpdate) {


        if (transactionDTO.getTransactionType().equals("Income")) {
            transactionToUpdate.setTransactionType(TransactionType.INCOME);
        } else if (transactionDTO.getTransactionType().equals("Expense")) {
            transactionToUpdate.setTransactionType(TransactionType.EXPENSE);
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        transactionToUpdate.setTransactionName(transactionDTO.getTransactionName());
        transactionToUpdate.setTransactionDate(transactionDTO.getTransactionDate());
        transactionToUpdate.setTransactionAmount(transactionDTO.getTransactionAmount());
        transactionToUpdate.setTransactionCategory(transactionDTO.getTransactionCategory());
        transactionToUpdate.setTransactionDescription(transactionDTO.getTransactionDescription());
        transactionToUpdate.setTransactionLastSaved(new Timestamp(System.currentTimeMillis()));

        return transactionToUpdate;
    }

    private boolean areTransactionsEqual(TransactionDTO transactionDTO, Transaction transaction) {

        TransactionType type;
        if (transactionDTO.getTransactionType().equals("Income")) {
            type = TransactionType.INCOME;
        } else {
            type = TransactionType.EXPENSE;
        }

        if (!(type.equals(transaction.getTransactionType())
                && transactionDTO.getTransactionName().equals(transaction.getTransactionName())
                && transactionDTO.getTransactionDate().equals(transaction.getTransactionDate())
                && transactionDTO.getTransactionAmount() == transaction.getTransactionAmount()
                && transactionDTO.getTransactionCategory().equals(transaction.getTransactionCategory())
                && transactionDTO.getTransactionDescription().equals(transaction.getTransactionDescription()))) {
            return false;
        }

        return true;
    }
}
