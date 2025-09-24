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
import {AppstateService} from "../../services/appstate-service/appstate.service";

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



  constructor(protected jwtService: JwtService, private userService: UserService, private transactionstateService: TransactionstateService, private appstateService: AppstateService, private router: Router) {

  }

  ngOnInit() {

    if (this.user == "") {
      this.userService.getAuthUser().subscribe({

        next: (data: any) => {
          this.appstateService.setAuthUser(data.authUser);
          this.user = this.appstateService.getAuthUser();
        },

        error: (err: any) => {

        }
      });
    } else {
      this.user = this.appstateService.getAuthUser();
    }

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
