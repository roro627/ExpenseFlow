import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AppRole } from '../models/roles';

const ROLE_KEY = 'expenseflow-role';

@Injectable({ providedIn: 'root' })
export class RoleService {
  private roleSubject = new BehaviorSubject<AppRole | null>(this.readFromStorage());
  readonly role$ = this.roleSubject.asObservable();

  get role(): AppRole | null {
    return this.roleSubject.value;
  }

  setRole(role: AppRole) {
    this.roleSubject.next(role);
    localStorage.setItem(ROLE_KEY, role);
  }

  clearRole() {
    this.roleSubject.next(null);
    localStorage.removeItem(ROLE_KEY);
  }

  private readFromStorage(): AppRole | null {
    const stored = localStorage.getItem(ROLE_KEY) as AppRole | null;
    return stored ?? null;
  }
}
