import { Component } from '@angular/core';
import { NgForOf } from '@angular/common';
import { RoleService } from '../../core/role.service';
import { AppRole, ROLE_LABELS } from '../../models/roles';
import { Router } from '@angular/router';

interface RoleCard {
  key: AppRole;
  description: string;
  helper: string;
}

@Component({
  selector: 'app-role-selector',
  standalone: true,
  imports: [NgForOf],
  templateUrl: './role-selector.component.html'
})
export class RoleSelectorComponent {
  readonly roles: RoleCard[] = [
    { key: 'employee', description: 'Submit expenses and purchase requests with receipts.', helper: 'Creates reports' },
    { key: 'manager', description: 'Review, approve or return submissions.', helper: 'Approves' },
    { key: 'finance', description: 'Handle payments, categories and exports.', helper: 'Pays & configures' }
  ];
  readonly labels = ROLE_LABELS;

  constructor(private roleService: RoleService, private router: Router) {}

  choose(role: AppRole) {
    this.roleService.setRole(role);
    this.router.navigate([`/${role}`]);
  }
}
