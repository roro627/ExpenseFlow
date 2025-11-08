import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AsyncPipe, NgClass, NgForOf, NgIf } from '@angular/common';
import { RoleService } from './core/role.service';
import { AppRole, ROLE_LABELS } from './models/roles';
import { NotificationService } from './core/notification.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, NgIf, AsyncPipe, NgForOf, NgClass],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  readonly role$ = this.roleService.role$;
  readonly message$ = this.notifications.message$;
  readonly roles: AppRole[] = ['employee', 'manager', 'finance'];
  readonly labels = ROLE_LABELS;

  constructor(private roleService: RoleService, private router: Router, private notifications: NotificationService) {}

  selectRole(role: AppRole) {
    this.roleService.setRole(role);
    this.router.navigate([`/${role}`]);
  }

  openRoleSelector() {
    this.roleService.clearRole();
    this.router.navigate(['/role']);
  }
}
