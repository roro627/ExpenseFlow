export type AppRole = 'employee' | 'manager' | 'finance';

export const ROLE_LABELS: Record<AppRole, string> = {
  employee: 'Employee',
  manager: 'Manager',
  finance: 'Finance'
};
