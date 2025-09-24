import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Transaction} from "../../models/transaction.model";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private saveTransactionsApiUrl: string = `${environment.API_BASE_URL}/transaction/savetransactions`;
  private getTransactionsApiUrl: string = `${environment.API_BASE_URL}/transaction/gettransactions`;

  constructor(private http: HttpClient) { }


  saveTransactions(transactions: Transaction[]): Observable<any> {

    const payload = { transactions: transactions};
    return this.http.post(`${this.saveTransactionsApiUrl}`, payload, {withCredentials: true});
  }

  getTransactions(): Observable<any> {
    return this.http.get(`${this.getTransactionsApiUrl}`, {withCredentials: true});

  }
}
