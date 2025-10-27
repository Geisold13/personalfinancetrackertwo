import { Component } from '@angular/core';
import {NgForOf, NgOptimizedImage} from "@angular/common";
import {TransactionstateService} from "../../services/transactionstate-service/transactionstate.service";
import {Transaction} from "../../models/transaction.model";
import {OnInit} from "@angular/core";
import {TransactionsComponent} from "../transactions/transactions.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgForOf,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{


  incomeByMonth: number[] = [];
  expenseByMonth: number[] = [];

  dashboardStatistics = {
    todayIncome: 0,
    todayExpenses: 0,
    weekToDateIncome: 0,
    weekToDateExpenses: 0,
    monthToDateIncome: 0,
    monthToDateExpenses: 0,
    yearToDateIncome: 0,
    yearToDateExpenses: 0
  };


  constructor(private transactionstateService: TransactionstateService) {

  }

  ngOnInit() {

      this.loadTransactions();

  }

  loadTransactions() {
    this.incomeByMonth = new Array(12).fill(0);
    this.expenseByMonth = new Array(12).fill(0)

    this.transactionstateService.fetchTransactions().subscribe({

      next: (data: any) => {
        const transactions: Transaction[] = data.transactions;
        this.calculateMonthlyIncomeExpense(transactions);
      },

      error: (err: any) => console.error(err)
    });
  }

  calculateMonthlyIncomeExpense(transactions: Transaction[]) {

    for (let i = 0; i < transactions.length; i++) {

      const date = new Date(transactions[i].transactionDate);
      const month: number = date.getMonth();

      if (transactions[i].transactionType == "Income") {
        this.incomeByMonth[month] = this.incomeByMonth[month] + transactions[i].transactionAmount;
      } else {
        this.expenseByMonth[month] = this.expenseByMonth[month] + Math.abs(transactions[i].transactionAmount);

      }
    }

  }

}
