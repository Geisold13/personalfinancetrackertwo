import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private currentUserSubject = new BehaviorSubject<any>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor() { }


  getCurrentUser(): string {
    return this.currentUserSubject.value;
  }

  setCurrentUser(user: string) {
    this.currentUserSubject.next(user);
  }

}
