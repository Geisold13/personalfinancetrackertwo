import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  private tokenKey:string = "jwt_token";
  private expirationKey:string = "jwt_expiration";

  constructor() {

  }

  saveToken(token:string):void {
    sessionStorage.setItem(this.tokenKey, token);
    sessionStorage.setItem(this.expirationKey, (new Date().getTime() + 60 * 60 * 1000).toString());
  }

  getToken():string | null {
    return sessionStorage.getItem(this.tokenKey);
  }

  getExpiration():string | null {
    return sessionStorage.getItem(this.expirationKey);
  }

  removeToken(): void {
    sessionStorage.removeItem(this.tokenKey);
    sessionStorage.removeItem(this.expirationKey);
  }

  isTokenValid(): boolean {

    if (this.getToken() && Number(this.getExpiration()) > new Date().getTime()) {

      return true;
    }

    return false;
  }

}
