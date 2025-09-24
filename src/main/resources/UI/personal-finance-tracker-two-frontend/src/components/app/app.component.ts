import {Component, OnInit, ViewChild} from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {AuthComponent} from "../auth/auth.component";
import {NgIf} from "@angular/common";
import {HttpClient, HttpClientModule, provideHttpClient, withInterceptorsFromDi} from "@angular/common/http";
import {DashboardComponent} from "../dashboard/dashboard.component";

import {AuthService} from "../../services/auth-service/auth.service";
import {JwtService} from "../../services/jwt-service/jwt.service";
import {UserService} from "../../services/user-service/user.service";
import {TransactionsComponent} from "../transactions/transactions.component";
import {TransactionstateService} from "../../services/transactionstate-service/transactionstate.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, AuthComponent, NgIf, HttpClientModule, DashboardComponent, TransactionsComponent, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'personal-finance-tracker-two-frontend';
  user: string = "";



  constructor(protected jwtService: JwtService, private userService: UserService, private transactionstateService: TransactionstateService, private router: Router) {

  }

  ngOnInit() {

    this.userService.currentUser$.subscribe(user =>{
      this.user = user;
    })

    if (!this.jwtService.isTokenValid()) {
      this.transactionstateService.clearTransactions();
      this.router.navigate(['/auth']);
    }
  }

  signout() {
    this.jwtService.removeToken();
    this.transactionstateService.clearTransactions();
    this.router.navigate(['/auth']);
  }




}
