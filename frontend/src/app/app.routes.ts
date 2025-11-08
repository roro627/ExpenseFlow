import { Routes } from '@angular/router';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { ManagerDashboardComponent } from './components/manager-dashboard/manager-dashboard.component';
import { FinanceDashboardComponent } from './components/finance-dashboard/finance-dashboard.component';
import { RoleSelectorComponent } from './components/role-selector/role-selector.component';
import { roleGuard } from './core/role.guard';

export const APP_ROUTES: Routes = [
  { path: '', redirectTo: 'role', pathMatch: 'full' },
  { path: 'role', component: RoleSelectorComponent },
  {
    path: 'employee',
    component: EmployeeDashboardComponent,
    canActivate: [roleGuard],
    data: { allowedRoles: ['employee'] }
  },
  {
    path: 'manager',
    component: ManagerDashboardComponent,
    canActivate: [roleGuard],
    data: { allowedRoles: ['manager'] }
  },
  {
    path: 'finance',
    component: FinanceDashboardComponent,
    canActivate: [roleGuard],
    data: { allowedRoles: ['finance'] }
  },
  { path: '**', redirectTo: 'role' }
];
