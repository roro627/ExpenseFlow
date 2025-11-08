import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { RoleService } from './role.service';
import { AppRole } from '../models/roles';

export const roleGuard: CanActivateFn = (route) => {
  const roleService = inject(RoleService);
  const router = inject(Router);
  const allowed = route.data?.['allowedRoles'] as AppRole[] | undefined;
  const current = roleService.role;

  if (!current) {
    router.navigateByUrl('/role');
    return false;
  }

  if (allowed && !allowed.includes(current)) {
    router.navigateByUrl(`/${current}`);
    return false;
  }

  return true;
};
