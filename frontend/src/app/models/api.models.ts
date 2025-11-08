export type RequestStatus = 'DRAFT' | 'SUBMITTED' | 'RETURNED' | 'APPROVED' | 'PAID';

export interface ExpenseItem {
  id?: number;
  txDate: string;
  amount: number;
  currency: string;
  category: string;
  costCenter: string;
  attachmentPath?: string;
  description?: string;
}

export interface ExpenseReport {
  id: number;
  title: string;
  employeeName: string;
  currency: string;
  total: number;
  status: RequestStatus;
  submittedAt?: string;
  paidAt?: string;
  managerComment?: string;
  financeNote?: string;
  createdAt?: string;
  updatedAt?: string;
  items: ExpenseItem[];
}

export interface ExpenseReportRequest {
  title: string;
  employeeName: string;
  currency: string;
  items: ExpenseItem[];
}

export interface PurchaseRequest {
  id: number;
  employeeName: string;
  vendor: string;
  total: number;
  justification: string;
  attachmentPath?: string;
  status: RequestStatus;
  submittedAt?: string;
  paidAt?: string;
  managerComment?: string;
  financeNote?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface PurchaseRequestRequest {
  employeeName: string;
  vendor: string;
  total: number;
  justification: string;
  attachmentPath?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}

export interface SettingsPayload {
  categories: string[];
  costCenters: string[];
}

export interface UploadResponse {
  path: string;
  originalName: string;
}
