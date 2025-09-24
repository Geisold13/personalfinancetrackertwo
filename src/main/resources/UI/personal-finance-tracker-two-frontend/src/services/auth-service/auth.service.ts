import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private signupApiUrl: string = `${environment.API_BASE_URL}/auth/register`;
  private signinApiUrl: string = `${environment.API_BASE_URL}/auth/authenticate`;

  constructor(private http: HttpClient) { }

  postSignupData(userData:any): Observable<any> {
    return this.http.post(`${this.signupApiUrl}`, userData);
  }

  postSigninData(userData:any): Observable<any> {
    return this.http.post(`${this.signinApiUrl}`, userData);
  }
}
