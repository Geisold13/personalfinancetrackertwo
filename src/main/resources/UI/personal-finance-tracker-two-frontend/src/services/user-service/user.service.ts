import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {BehaviorSubject, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private authUserApiUrl: string = `${environment.API_BASE_URL}/auth/user`;

  constructor(private http: HttpClient) { }

  getAuthUser(): Observable<any> {
    return this.http.get(`${this.authUserApiUrl}`, {withCredentials: true});
  }

}
