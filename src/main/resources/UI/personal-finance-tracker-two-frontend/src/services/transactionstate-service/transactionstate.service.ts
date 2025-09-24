import { Injectable } from '@angular/core';
import {Transaction} from "../../models/transaction.model";

@Injectable({
  providedIn: 'root'
})
export class TransactionstateService {

  private transactions: Transaction[] = [];

  constructor() { }

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
}
