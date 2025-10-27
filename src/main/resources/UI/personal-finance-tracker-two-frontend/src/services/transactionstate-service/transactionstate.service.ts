import { Injectable } from '@angular/core';
import {Transaction} from "../../models/transaction.model";
import {TransactionService} from "../transaction-service/transaction.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TransactionstateService {

  private transactions: Transaction[] = [];

  constructor(private transactionService: TransactionService) { }

  addTransaction(transaction: Transaction) {
    this.transactions.push(transaction);
  }

  getTransactions(): Transaction[] {
    return this.transactions;
  }

  setTransactions(transactions: Transaction[]) {
    this.transactions = transactions;
  }

  clearTransactions() {
    this.transactions = [];
  }

  public fetchTransactions(): Observable<Transaction[]> {
    return this.transactionService.getTransactions();
  }
}
