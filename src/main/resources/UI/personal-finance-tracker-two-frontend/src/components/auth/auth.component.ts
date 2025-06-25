import { Component } from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgIf
  ],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent {


  showSignUpScreen: boolean = true;
  showSignUpScreenCallToAction: boolean = false;
  showSignInScreen: boolean = false;
  showSignInScreenCallToAction: boolean = true;

  toggleSignUpScreen():void {
    this.showSignUpScreenCallToAction = false;
    this.showSignInScreenCallToAction = true;
    this.showSignUpScreen = true;
    this.showSignInScreen = false;
  }

  toggleSignInScreen():void {
    this.showSignInScreenCallToAction = false;
    this.showSignUpScreenCallToAction = true;
    this.showSignInScreen = true;
    this.showSignUpScreen = false;
  }

  onSignUpSubmit():void {

  }

  onSignInSubmit():void {

  }
}
