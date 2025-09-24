import {Component, EventEmitter, OnInit} from '@angular/core';
import {NgClass, NgIf, NgOptimizedImage, NgStyle} from "@angular/common";
import {SigninRequest} from "../../models/signin-request.model";
import {SignupRequest} from "../../models/signup-request.model";
import {AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth-service/auth.service";
import {Output} from "@angular/core";
import {Router} from "@angular/router";
import {JwtService} from "../../services/jwt-service/jwt.service";
import {UserService} from "../../services/user-service/user.service";

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgIf,
    ReactiveFormsModule,
    NgStyle,
    NgClass
  ],
  providers: [AuthService],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent implements OnInit {

  signupRequest: SignupRequest = {
    firstName: '',
    lastName: '',
    email: '',
    password: ''
  };

  signinRequest: SigninRequest = {
    email: '',
    password: ''
  }

  // ! tells typescript these will be initialized before they are used.
  signupForm!: FormGroup;
  signinForm!: FormGroup;



  showSignUpScreen: boolean = true;
  showSignUpScreenCallToAction: boolean = false;
  showSignInScreen: boolean = false;
  showSignInScreenCallToAction: boolean = true;

  signupSuccessResponseMessage: string = "";
  signupErrorResponseMessage: string = "";
  signinSuccessResponseMessage: string = "";
  signinErrorResponseMessage: string = "";


  constructor(private formBuilder: FormBuilder, private authService: AuthService, private userService: UserService, private jwtService: JwtService, private router: Router) {
  }

  ngOnInit(): void {

    this.signupForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('[A-Za-z]*$')]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('[A-Za-z]*$')]],
      email: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(256), Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(256)]],
      confirmPassword: ['', [Validators.required]]
    });

    this.signinForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(256), Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(256)]],
    });
  }

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

  signIn(token: string) {
    this.jwtService.saveToken(token);
    this.router.navigate(['/dashboard']);
  }

  getSigninSignupInputValidation(control: AbstractControl | null) {

    // if the control (the input field) is null or undefined, or if the control is not dirty (nothing has been typed yet) or if the control is empty ('', null, undefined, or 0)
    // no css classes are returned to the [ngClass] and the default black border will be there.
    if (!control || !control.dirty || !control.value) {
      return {};
    }

    return {'valid-border': control?.valid, 'invalid-border': control?.invalid && control?.dirty};
  }

  comparePasswordsValidation(controlOne: AbstractControl | null, controlTwo: AbstractControl | null) {

    if (!controlOne || !controlOne.dirty || !controlOne.value) {
      return {};
    }

    return {'valid-border': controlOne?.value === controlTwo?.value, 'invalid-border': controlOne?.value != controlTwo?.value};
  }

  showPasswordValidationSuccessMessage(controlOne: AbstractControl | null, controlTwo: AbstractControl | null): boolean | undefined {
    if (!controlOne || !controlOne.dirty || !controlOne.value) {
      return false;
    }

    if (this.signupForm.get('password')?.value === this.signupForm.get('confirmPassword')?.value) {
      return true;
    }

    return false;
  }

  showPasswordValidationErrorMessage(controlOne: AbstractControl | null, controlTwo: AbstractControl | null) {
    if (!controlOne || !controlOne.dirty || !controlOne.value) {
      return false;
    }

    if (this.signupForm.get('password')?.value !== this.signupForm.get('confirmPassword')?.value) {
      return true;
    }

    return false;
  }

  onSignUpSubmit():void {

    if (this.signupForm.valid && this.signupForm.get('password')?.value === this.signupForm.get('confirmPassword')?.value) {

      this.signupRequest.firstName = this.signupForm.get("firstName")?.value;
      this.signupRequest.lastName = this.signupForm.get("lastName")?.value;
      this.signupRequest.email = this.signupForm.get("email")?.value;
      this.signupRequest.password = this.signupForm.get("password")?.value;

      this.authService.postSignupData(this.signupRequest).subscribe({

        next: (response:any):void => {
          this.signupSuccessResponseMessage = response.message;
          this.signupErrorResponseMessage = "";
        },

        error: (err:any):void => {
          this.signupErrorResponseMessage = err.error?.message || "An error occurred.";
          this.signupSuccessResponseMessage = "";
        }
      });
    }
  }

  onSignInSubmit():void {

    if (this.signinForm.valid) {

      this.signinRequest.email = this.signinForm.get("email")?.value;
      this.signinRequest.password = this.signinForm.get("password")?.value;

      this.authService.postSigninData(this.signinRequest).subscribe({

        next: (response:any):void => {
          this.signinSuccessResponseMessage = response.message;
          this.userService.setCurrentUser(response.user);
          console.log(this.userService.getCurrentUser());
          this.signIn(response.token);
        },

        error: (err:any):void => {
          this.signinErrorResponseMessage = err.error?.message || "An error occurred.";
        }
      });
    }
  }


}
