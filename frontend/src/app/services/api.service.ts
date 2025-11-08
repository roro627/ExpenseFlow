import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import {
  ExpenseReport,
  ExpenseReportRequest,
  PageResponse,
  PurchaseRequest,
  PurchaseRequestRequest,
  RequestStatus,
  SettingsPayload,
  UploadResponse
} from '../models/api.models';
import { environment } from '../../environments/environment';

interface ListParams {
  status?: RequestStatus;
  employee?: string;
  page?: number;
  size?: number;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getExpenseReports(params?: ListParams) {
    return this.http
      .get<PageResponse<ExpenseReport>>(`${this.baseUrl}/reports`, { params: this.buildParams(params) })
      .pipe(catchError(this.handleError));
  }

  createExpenseReport(payload: ExpenseReportRequest) {
    return this.http
      .post<ExpenseReport>(`${this.baseUrl}/reports`, payload)
      .pipe(catchError(this.handleError));
  }

  updateExpenseReport(id: number, payload: ExpenseReportRequest) {
    return this.http
      .put<ExpenseReport>(`${this.baseUrl}/reports/${id}`, payload)
      .pipe(catchError(this.handleError));
  }

  submitExpenseReport(id: number) {
    return this.http
      .post<ExpenseReport>(`${this.baseUrl}/reports/${id}/submit`, {})
      .pipe(catchError(this.handleError));
  }

  managerDecisionOnReport(id: number, status: RequestStatus, comment?: string) {
    return this.http
      .post<ExpenseReport>(`${this.baseUrl}/reports/${id}/manager`, { status, comment })
      .pipe(catchError(this.handleError));
  }

  markReportPaid(id: number, note: string) {
    return this.http
      .post<ExpenseReport>(`${this.baseUrl}/reports/${id}/finance/pay`, { note })
      .pipe(catchError(this.handleError));
  }

  getPurchaseRequests(params?: ListParams) {
    return this.http
      .get<PageResponse<PurchaseRequest>>(`${this.baseUrl}/purchases`, { params: this.buildParams(params) })
      .pipe(catchError(this.handleError));
  }

  createPurchaseRequest(payload: PurchaseRequestRequest) {
    return this.http
      .post<PurchaseRequest>(`${this.baseUrl}/purchases`, payload)
      .pipe(catchError(this.handleError));
  }

  updatePurchaseRequest(id: number, payload: PurchaseRequestRequest) {
    return this.http
      .put<PurchaseRequest>(`${this.baseUrl}/purchases/${id}`, payload)
      .pipe(catchError(this.handleError));
  }

  submitPurchaseRequest(id: number) {
    return this.http
      .post<PurchaseRequest>(`${this.baseUrl}/purchases/${id}/submit`, {})
      .pipe(catchError(this.handleError));
  }

  managerDecisionOnPurchase(id: number, status: RequestStatus, comment?: string) {
    return this.http
      .post<PurchaseRequest>(`${this.baseUrl}/purchases/${id}/manager`, { status, comment })
      .pipe(catchError(this.handleError));
  }

  markPurchasePaid(id: number, note: string) {
    return this.http
      .post<PurchaseRequest>(`${this.baseUrl}/purchases/${id}/finance/pay`, { note })
      .pipe(catchError(this.handleError));
  }

  getSettings() {
    return this.http.get<SettingsPayload>(`${this.baseUrl}/settings`).pipe(catchError(this.handleError));
  }

  updateSettings(payload: SettingsPayload) {
    return this.http.put<SettingsPayload>(`${this.baseUrl}/settings`, payload).pipe(catchError(this.handleError));
  }

  uploadReceipt(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<UploadResponse>(`${this.baseUrl}/uploads`, formData).pipe(catchError(this.handleError));
  }

  downloadReportsCsv() {
    return this.http
      .get(`${this.baseUrl}/finance/export/reports`, { responseType: 'blob' as const })
      .pipe(catchError(this.handleError));
  }

  private buildParams(params?: ListParams) {
    let httpParams = new HttpParams();
    if (!params) {
      return httpParams;
    }
    Object.entries(params).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        httpParams = httpParams.set(key, value as string);
      }
    });
    return httpParams;
  }

  private handleError(err: HttpErrorResponse) {
    const message = err.error?.message || 'Request failed';
    return throwError(() => new Error(message));
  }
}
