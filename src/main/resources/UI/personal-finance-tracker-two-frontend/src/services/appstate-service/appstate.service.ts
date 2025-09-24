import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppstateService {

  authUser: string = "";
  constructor() { }


  setAuthUser(user: string) {
    this.authUser = user;
  }

  getAuthUser(): string {
    return this.authUser;
  }
}
