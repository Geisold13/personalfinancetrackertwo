import { Routes } from '@angular/router';
import {DashboardComponent} from "../dashboard/dashboard.component";
import {TransactionsComponent} from "../transactions/transactions.component";
import {BudgetsComponent} from "../budgets/budgets.component";
import {ReportsComponent} from "../reports/reports.component";
import {GoalsComponent} from "../goals/goals.component";
import {HelpingComponent} from "../helping/helping.component";
import {AboutComponent} from "../about/about.component";
import {SettingsComponent} from "../settings/settings.component";
import {AuthComponent} from "../auth/auth.component";
import {authGuard} from "../../guard/auth/auth.guard";
import {signinGuard} from "../../guard/signin/signin.guard";
import {NotificationsComponent} from "../notifications/notifications.component";

export const routes: Routes = [
  {path: 'auth', component: AuthComponent, canActivate: [signinGuard]},
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {path: 'dashboard', component: DashboardComponent},
      {path: 'transactions', component: TransactionsComponent},
      {path: 'budgets', component: BudgetsComponent},
      {path: 'reports', component: ReportsComponent},
      {path: 'goals', component: GoalsComponent},
      {path: 'notifications', component: NotificationsComponent},
      {path: 'helping', component: HelpingComponent},
      {path: 'about', component: AboutComponent},
      {path: 'settings', component: SettingsComponent},
      {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
    ]
  },
  {path: "**", redirectTo: 'dashboard'}
];
