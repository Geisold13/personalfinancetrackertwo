import {Component, OnInit} from '@angular/core';
import {Transaction} from "../../models/transaction.model";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {TransactionService} from "../../services/transaction-service/transaction.service";
import {TransactionstateService} from "../../services/transactionstate-service/transactionstate.service";
import {MatSelectModule} from "@angular/material/select";
import {MatFormFieldModule} from "@angular/material/form-field";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {TransactionreportService} from "../../services/transactionreport-service/transactionreport.service";

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgForOf,
    NgIf,
    FormsModule,
  ],
  providers: [TransactionService],
  templateUrl: './transactions.component.html',
  styleUrl: './transactions.component.css',
})
export class TransactionsComponent implements OnInit{

  addTransaction: FormGroup;
  generateReport: FormGroup;
  transactions: Transaction[] = [];
  selectedTransactionRowIndex: number | null = null;
  transactionSortSelection: string = "";

  protected transactionStats = {
    totalIncome: 0,
    totalExpenses: 0,
    totalTransactions: 0,
    netSavings: 0,
    averageExpense: 0,
    topExpenseCategory: ""
  };

  protected transactionFilters = {
    type: "",
    category: "",
    minAmount: null as number | null,
    maxAmount: null as number | null,
    startDate: "",
    endDate: ""
  };


  protected transactionUIState = {
    isCrudButtonDisabled: false,
    isEditingExistingTransaction: false,
    areTransactionsInSync: false,
    areTransactionsBeingFiltered: false,

    sort: {
      isSortSelectionDisabled: false,
      isSortButtonDisabled: false,
      isSortResetButtonDisabled: false
    },
    messages: {
      showFormError: false,
      showFormSuccess: false,
      showDeleteError: false,
      showDeleteSuccess: false,
      showEditError: false,
      showEditSuccess: false,
      showInSync: false,
      showNotInSync: false,
      showSaveSuccess: false,
      showSaveNotNecessary: false,
      showFormTypeAmountMismatch: false,
      showBeingSorted: false,
      showSortSelectionDisabled: false,
      showGenerateReportSuccess: false,
      showGenerateReportError: false,
      showGenerateReportErrorNoName: false

    },
    popup: {
      showGenerateReport: false
    }
  };

  protected readonly transactionMessages = {
    addTransactionFormError:  "All fields need to be filled out correctly before submitting.",
    addTransactionFormSuccess:  "Transaction Added!",
    deleteTransactionError:  "No transaction selected. Select a transaction to delete.",
    deleteTransactionSuccess:  "Transaction Successfully deleted.",
    editTransactionError: "No transaction selected. Select a transaction to edit.",
    editTransactionSuccess: "Transaction Successfully edited.",
    transactionInSync: "Transactions are up to date with latest save.",
    transactionNotInSync: "You have unsaved changes. Save changes for stats to update.",
    saveTransactionsSuccess: "Transactions Saved Successfully!",
    saveTransactionNotNecessary: "Transactions already up to date.",
    addTransactionFormTypeAmountMismatch: "Amount needs to be > 0 for Income type & Amount needs to be < 0 for Expense type.",
    generateReportSuccess: "Report generated and saved!",
    generateReportError: "Error generating report.",
    generateReportErrorNoName: "Report needs a name before generating."

  } as const;

  constructor(private transactionService: TransactionService, private transactionstateService: TransactionstateService, private transactionreportService: TransactionreportService) {

    // new FormGroup for adding a transaction is initialized with each FormControl representing an input field with specific validation constraints for each
    this.addTransaction = new FormGroup({
      transactionType: new FormControl('', [Validators.required, Validators.pattern("^[A-Za-z]+$")]),
      transactionName: new FormControl('', [Validators.required, Validators.maxLength(100), Validators.pattern("^[A-Za-z ]+$")]),
      transactionAmount: new FormControl('', [Validators.required, Validators.min(-1000000000), Validators.max(1000000000)]),
      transactionDate: new FormControl('', [Validators.required,  Validators.pattern("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")]),
      transactionCategory: new FormControl('', [Validators.required, Validators.pattern("^[A-Za-z]+$")]),
      transactionDescription: new FormControl('', [Validators.required, Validators.maxLength(255), Validators.pattern("^[A-Za-z ]+$")])
    });

    this.generateReport = new FormGroup({
      reportName: new FormControl('', [Validators.required]),
      reportDescription: new FormControl('')
    });
  }

  /**
   * On Component Creation
   */
  ngOnInit() {

    // on component creation, checks if there are any transactions loaded in from the db yet, if not, loads them in, otherwise
    // sets the transactions array in the component to the transactions from the db in the TransactionstateService
    if (this.transactionstateService.getTransactions().length === 0) {
      this.getTransactions();
    } else {
      this.transactions = JSON.parse(JSON.stringify(this.transactionstateService.getTransactions()));
      this.calculateTransactionStats();
    }
    this.checkTransactionStateSynced(this.transactions, this.transactionstateService.getTransactions());
  }

  public selectTransactionRow(index: number): void {
    // checks if row at index was already selected, if so, the index become null, otherwise assigns the new index.
    this.selectedTransactionRowIndex = this.selectedTransactionRowIndex === index ? null : index;
  }

  /**
   * Checks to see if the transaction type matches the rules of what bounds the transactionAmount
   * Income has transactionAmount > 0 and Expense has transactionAmount < 0.
   */
  public checkAmountValidity(): boolean {

    if (this.addTransaction.get('transactionType')?.value == "Income" && this.addTransaction.get('transactionAmount')?.value > 0) {
      return true;
    } else if (this.addTransaction.get('transactionType')?.value == "Expense" && this.addTransaction.get('transactionAmount')?.value < 0) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * Compares the transactions array currently loaded into the component, and compares it with the transactions array last saved and loaded in the from the db
   * returns a boolean, true if equal, false is not
   * @param clientTransactions
   * @param transactionsOnLastSave
   */
  public checkTransactionStateSynced(clientTransactions: Transaction[], transactionsOnLastSave: Transaction[]): void{

    if ((clientTransactions.length != transactionsOnLastSave.length) || (JSON.stringify(clientTransactions)) !== JSON.stringify(transactionsOnLastSave)) {
      this.transactionUIState.areTransactionsInSync = false;
      this.transactionUIState.messages.showInSync = false;
      this.transactionUIState.messages.showNotInSync = true;
    } else {
      this.transactionUIState.areTransactionsInSync = true;
      this.transactionUIState.messages.showInSync = true;
      this.transactionUIState.messages.showNotInSync = false;
    }
  }

  /**
   * Checks if the current array of Transactions displayed within the Transactions component match exactly with the array of Transactions within the transactionstateservice that were last loaded from the db
   * @param clientTransactions
   * @param transactionsOnLastSave
   */
  public isTransactionStateSynced(clientTransactions: Transaction[], transactionsOnLastSave: Transaction[]): boolean{
    return (clientTransactions.length === transactionsOnLastSave.length) && (JSON.stringify(clientTransactions) === JSON.stringify(transactionsOnLastSave));
  }


  /**
   * When user clicks "submit" on the add transactions form this function runs.
   */
  public onTransactionSubmit() {

    // This conditional is when a user is editing an existing transaction.
    if (this.addTransaction.valid && this.checkAmountValidity() && this.transactionUIState.isEditingExistingTransaction && typeof this.selectedTransactionRowIndex === "number") {

      this.transactions[this.selectedTransactionRowIndex] = {
        transactionID: this.transactions[this.selectedTransactionRowIndex].transactionID,
        transactionType: this.addTransaction.get('transactionType')?.value,
        transactionName: this.addTransaction.get('transactionName')?.value,
        transactionAmount: this.addTransaction.get('transactionAmount')?.value,
        transactionDate: this.addTransaction.get('transactionDate')?.value,
        transactionCategory: this.addTransaction.get('transactionCategory')?.value,
        transactionDescription: this.addTransaction.get('transactionDescription')?.value
      };

      this.transactionUIState.isEditingExistingTransaction = false;

      setTimeout(() => {
        this.selectedTransactionRowIndex = null;
      }, 1000)

      this.checkTransactionStateSynced(this.transactions, this.transactionstateService.getTransactions());
      return;
    }

    // This conditional runs when user is adding a new transaction and not an existing transaction.
    if (this.addTransaction.valid && this.checkAmountValidity() && !this.transactionUIState.isEditingExistingTransaction) {

      const newTransaction: Transaction = {
        transactionID: 0,
        transactionType: this.addTransaction.get('transactionType')?.value,
        transactionName: this.addTransaction.get('transactionName')?.value,
        transactionAmount: this.addTransaction.get('transactionAmount')?.value,
        transactionDate: this.addTransaction.get('transactionDate')?.value,
        transactionCategory: this.addTransaction.get('transactionCategory')?.value,
        transactionDescription: this.addTransaction.get('transactionDescription')?.value
      };

      this.transactions.push(newTransaction);
      this.checkTransactionStateSynced(this.transactions, this.transactionstateService.getTransactions());
      this.resetAddTransactionForm();

      this.transactionUIState.messages.showFormSuccess = true;
      this.transactionUIState.messages.showFormError = false;
      this.transactionUIState.messages.showFormTypeAmountMismatch = false;

      // if transaction fields are valid, but the transaction type doesn't match the transaction amount
    } else if (this.addTransaction.valid && !this.checkAmountValidity()) {
      this.transactionUIState.messages.showFormTypeAmountMismatch = true;
      this.transactionUIState.messages.showFormError = false;
      this.transactionUIState.messages.showFormSuccess = false;
      // if no fields are valid or any other errors.
    } else {
      this.transactionUIState.messages.showFormError = true;
      this.transactionUIState.messages.showFormSuccess = false;
      this.transactionUIState.messages.showFormTypeAmountMismatch = false;
    }

    setTimeout(() => {
      this.transactionUIState.messages.showFormSuccess = false;
      this.transactionUIState.messages.showFormError = false;
      this.transactionUIState.messages.showFormTypeAmountMismatch = false;
    }, 3000);

  }

  // resets add transaction form so no messages are showing.
  public onTransactionReset(): void {
    this.transactionUIState.messages.showFormSuccess = false;
    this.transactionUIState.messages.showFormError = false;
    this.transactionUIState.messages.showFormTypeAmountMismatch = false;
  }

  // resets the add transaction form fields
  public resetAddTransactionForm(): void {
    this.addTransaction.reset({
      transactionType: '',
      transactionName: '',
      transactionAmount: '',
      transactionDate: '',
      transactionCategory: '',
      transactionDescription: ''
    });
  }

  public onSaveTransactions(): void {

    if (!this.transactionUIState.areTransactionsInSync) {
      this.transactionService.saveTransactions(this.transactions).subscribe({

        next: (data: any) => {

          this.transactionstateService.setTransactions(data.transactions);
          this.transactions = JSON.parse(JSON.stringify(this.transactionstateService.getTransactions()));

          this.checkTransactionStateSynced(this.transactions, this.transactionstateService.getTransactions());
          this.calculateTransactionStats();

          this.transactionUIState.messages.showSaveSuccess = true;
          this.transactionUIState.messages.showSaveNotNecessary = false;
          this.transactionUIState.messages.showSortSelectionDisabled = false;

          this.transactionUIState.sort.isSortSelectionDisabled = false;
          this.transactionUIState.sort.isSortButtonDisabled = false;
          this.transactionUIState.sort.isSortResetButtonDisabled = false;

          setTimeout(() => {
            this.transactionUIState.messages.showSaveSuccess = false;
          }, 3000);
        },

        error: (error: any) => {

        }
      });

    } else {

      this.transactionUIState.messages.showSaveSuccess = false;
      this.transactionUIState.messages.showSaveNotNecessary = true;

      setTimeout(() => {
        this.transactionUIState.messages.showSaveNotNecessary = false;
      }, 3000);
    }
  }

  public getTransactions(): void {

    this.transactionService.getTransactions().subscribe({

      next: (data: any) => {
        this.transactionstateService.setTransactions(data.transactions);
        this.transactions = JSON.parse(JSON.stringify(this.transactionstateService.getTransactions()));
        this.calculateTransactionStats();
      },

      error: (error: any) => {

      }
    });
  }

  public onDeleteTransaction(): void {

    // if there's no row selected, error message pops up, and function is stopped
    if (this.selectedTransactionRowIndex == null) {
      this.transactionUIState.messages.showDeleteError = true;

      setTimeout(() => {
        this.transactionUIState.messages.showDeleteError = false;
      }, 3000);

      return;

    } else {

      this.transactions.splice(this.selectedTransactionRowIndex, 1);
      this.checkTransactionStateSynced(this.transactions, this.transactionstateService.getTransactions());

      this.transactionUIState.messages.showDeleteError = false;
      this.transactionUIState.messages.showDeleteSuccess = true;
      this.selectedTransactionRowIndex = null;

      setTimeout(() => {
        this.transactionUIState.messages.showDeleteSuccess = false;
      }, 3000);
    }


  }

  public onEditTransaction(): void {

    if (this.selectedTransactionRowIndex == null) {

      this.transactionUIState.messages.showEditError = true;

      setTimeout(() => {
        this.transactionUIState.messages.showEditError = false;
      }, 3000);
      return;

    } else {

      this.addTransaction.get('transactionType')?.setValue(this.transactions[this.selectedTransactionRowIndex].transactionType);
      this.addTransaction.get('transactionName')?.setValue(this.transactions[this.selectedTransactionRowIndex].transactionName);
      this.addTransaction.get('transactionAmount')?.setValue(this.transactions[this.selectedTransactionRowIndex].transactionAmount);
      this.addTransaction.get('transactionDate')?.setValue(this.transactions[this.selectedTransactionRowIndex].transactionDate);
      this.addTransaction.get('transactionCategory')?.setValue(this.transactions[this.selectedTransactionRowIndex].transactionCategory);
      this.addTransaction.get('transactionDescription')?.setValue(this.transactions[this.selectedTransactionRowIndex].transactionDescription);

      this.transactionUIState.isEditingExistingTransaction = true;
    }
  }

  public onClickSortSelector(): void {

    if ((!this.transactionUIState.areTransactionsBeingFiltered && !this.isTransactionStateSynced(this.transactions, this.transactionstateService.getTransactions()))) {
      this.transactionUIState.sort.isSortSelectionDisabled = true;
      this.transactionUIState.sort.isSortButtonDisabled = true;
      this.transactionUIState.sort.isSortResetButtonDisabled = true;
      this.transactionUIState.messages.showSortSelectionDisabled = true;
    } else {
      this.transactionUIState.sort.isSortSelectionDisabled = false;
      this.transactionUIState.messages.showSortSelectionDisabled = false;
      this.transactionUIState.sort.isSortButtonDisabled = false;
      this.transactionUIState.sort.isSortResetButtonDisabled = false;
    }
  }

  public onSort(): void{
    if (this.transactionSortSelection != "") {
      this.transactionUIState.sort.isSortSelectionDisabled = true;
      this.transactionUIState.messages.showBeingSorted = true;
      this.transactionUIState.isCrudButtonDisabled = true;
    }

    if (this.transactionSortSelection == "type") {
      this.transactions.sort((a: Transaction, b: Transaction): number => a.transactionType.localeCompare(b.transactionType));
    }

    if (this.transactionSortSelection == "name-alpha") {
      this.transactions.sort((a: Transaction, b: Transaction): number => a.transactionName.localeCompare(b.transactionName));
    }

    if (this.transactionSortSelection == "amount-asc") {
      this.transactions.sort((a: Transaction, b: Transaction): number => a.transactionAmount - b.transactionAmount);
    }

    if (this.transactionSortSelection == "amount-desc") {
      this.transactions.sort((a: Transaction, b: Transaction): number => b.transactionAmount - a.transactionAmount);
    }

    if (this.transactionSortSelection == "date-newest") {
      this.transactions.sort((a: Transaction, b: Transaction): number => new Date(b.transactionDate).getTime() - new Date(a.transactionDate).getTime());
    }

    if (this.transactionSortSelection == "date-oldest") {
      this.transactions.sort((a: Transaction, b: Transaction): number => new Date(a.transactionDate).getTime() - new Date(b.transactionDate).getTime());
    }

    if (this.transactionSortSelection == "category-alpha") {
      this.transactions.sort((a: Transaction, b: Transaction): number => a.transactionCategory.localeCompare(b.transactionCategory));

    }
  }

  public calculateTransactionStats(): void {
    this.transactionStats.totalIncome = this.calculateTotalIncome();
    this.transactionStats.totalExpenses = this.calculateTotalExpenses();
    this.transactionStats.totalTransactions = this.calculateTotalTransactions();
    this.transactionStats.averageExpense = this.calculateAverageExpense();
    this.transactionStats.netSavings = this.calculateNetSavings();
    this.transactionStats.topExpenseCategory = this.calculateTopExpenseCategory();
  }

  public onFilter(): void {
    this.transactionUIState.areTransactionsBeingFiltered = true;
    this.transactionUIState.isCrudButtonDisabled = true;

    let filteredTransactions: Transaction[] = this.transactions.filter(t => {

      if (this.transactionFilters.type && t.transactionType != this.transactionFilters.type) {
        return false;
      }

      if (this.transactionFilters.category && t.transactionCategory != this.transactionFilters.category) {
        return false;
      }

      if (this.transactionFilters.minAmount !== null && t.transactionAmount < this.transactionFilters.minAmount) {
        return false;
      }
      if (this.transactionFilters.maxAmount !== null && t.transactionAmount > this.transactionFilters.maxAmount) {
        return false;
      }

      if (this.transactionFilters.startDate && new Date(t.transactionDate) < new Date(this.transactionFilters.startDate)) {
        return false;
      }
      if (this.transactionFilters.endDate && new Date(t.transactionDate) > new Date(this.transactionFilters.endDate)) {
        return false;
      }

      return true;
    });

    this.transactions = JSON.parse(JSON.stringify(filteredTransactions));
  }

  public onFilterSortReset(): void {
    this.getTransactions();
    this.transactionFilters.type = "";
    this.transactionFilters.category = "";
    this.transactionFilters.minAmount = null;
    this.transactionFilters.maxAmount = null;
    this.transactionFilters.startDate = "";
    this.transactionFilters.endDate = "";

    this.transactionUIState.areTransactionsBeingFiltered = false;
    this.transactionUIState.isCrudButtonDisabled = false;
    this.transactionUIState.sort.isSortButtonDisabled = false;
    this.transactionUIState.sort.isSortSelectionDisabled = false;
  }

  public onGenerateReport(): void {
    this.transactionUIState.popup.showGenerateReport = true;
  }

  public generateTransactionReport(): void {

    if (this.generateReport.valid) {

      const payload = {
        transactionReportName: this.generateReport.get('reportName')?.value,
        transactionReportDescription: this.generateReport.get('reportDescription')?.value,
        transactions: this.transactions
      };

      this.transactionreportService.saveTransactionReport(payload).subscribe({

        next: (data: any) => {
          this.transactionUIState.messages.showGenerateReportSuccess = true;
          this.transactionUIState.messages.showGenerateReportError = false;
          this.transactionUIState.messages.showGenerateReportErrorNoName = false;
        },

        error: (err: any) => {
          this.transactionUIState.messages.showGenerateReportError = true;
          this.transactionUIState.messages.showGenerateReportSuccess = false;
        }
      });

    } else {
      this.transactionUIState.messages.showGenerateReportErrorNoName = true;
    }

    setTimeout(() => {
      this.generateReport.reset({
        reportName: '',
        reportDescription: ''
      });
    }, 3000);

  }

  public onCloseGenerateReport() {
    this.transactionUIState.popup.showGenerateReport = false;
    this.transactionUIState.messages.showGenerateReportSuccess = false;
    this.transactionUIState.messages.showGenerateReportError = false;
    this.transactionUIState.messages.showGenerateReportErrorNoName = false;
  }

  private calculateTotalIncome(): number {
    let totalIncome: number = 0;

    for (let i = 0; i < this.transactions.length; i++) {
      if (this.transactions[i].transactionType === "Income") {
        totalIncome = totalIncome + this.transactions[i].transactionAmount;
      }
    }
    return totalIncome;
  }

  private calculateTotalExpenses(): number {
    let totalExpenses: number = 0;

    for (let i = 0; i < this.transactions.length; i++) {
      if (this.transactions[i].transactionType === "Expense") {
        totalExpenses = totalExpenses + this.transactions[i].transactionAmount;
      }
    }
    return totalExpenses;
  }

  private calculateTotalTransactions(): number {
    return this.transactions.length;
  }

  private calculateAverageExpense(): number {
    let expenseCount: number = 0;
    for (let i = 0; i < this.transactions.length; i++) {
      if (this.transactions[i].transactionType === "Expense") {
        expenseCount++
      }
    }
    return Math.round((this.calculateTotalExpenses() / expenseCount) * 100) / 100;
  }

  private calculateNetSavings(): number {
    return this.calculateTotalIncome() + this.calculateTotalExpenses();
  }

  private calculateTopExpenseCategory() {
    let categoryHashMap: Map<string, number> = new Map();

    for (let i = 0; i < this.transactions.length; i++) {

      if (this.transactions[i].transactionType == "Expense" && !categoryHashMap.has(this.transactions[i].transactionCategory)) {
        categoryHashMap.set(this.transactions[i].transactionCategory, this.transactions[i].transactionAmount);
      } else if (this.transactions[i].transactionType == "Expense" && categoryHashMap.has(this.transactions[i].transactionCategory)) {
        categoryHashMap.set(this.transactions[i].transactionCategory, <number>categoryHashMap.get(this.transactions[i].transactionCategory) + this.transactions[i].transactionAmount);
      }
    }
    const entries = Array.from(categoryHashMap.entries());
    const [topCategory, topAmount] = entries.reduce((previous: [string, number], current: [string, number]): [string, number] => Math.abs(current[1]) > Math.abs(previous[1]) ? current : previous);
    return topCategory;
  }
}
