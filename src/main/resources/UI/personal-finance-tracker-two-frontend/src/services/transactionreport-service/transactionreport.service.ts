import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TransactionreportService {

  private saveTransactionreportApiUrl: string = `${environment.API_BASE_URL}/transactionreport/savetransactionreport`;

  constructor(private http: HttpClient) { }

  saveTransactionReport(data: any): Observable<any> {

    return this.http.post(`${this.saveTransactionreportApiUrl}`, data, {withCredentials: true});
  }

}
