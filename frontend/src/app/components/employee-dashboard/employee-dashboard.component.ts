import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe, NgForOf, NgIf } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { ExpenseItem, ExpenseReport, ExpenseReportRequest, PurchaseRequest, PurchaseRequestRequest } from '../../models/api.models';
import { NotificationService } from '../../core/notification.service';

@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgIf, NgForOf, CurrencyPipe, DatePipe],
  templateUrl: './employee-dashboard.component.html'
})
export class EmployeeDashboardComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(ApiService);
  private readonly notifications = inject(NotificationService);

  reports: ExpenseReport[] = [];
  purchases: PurchaseRequest[] = [];
  categories: string[] = [];
  costCenters: string[] = [];

  editingReportId: number | null = null;
  editingPurchaseId: number | null = null;

  reportForm: FormGroup = this.fb.group({
    title: ['', Validators.required],
    employeeName: ['', Validators.required],
    currency: ['USD', [Validators.required, Validators.pattern(/^[A-Z]{3}$/)]],
    items: this.fb.array([this.createItemGroup()])
  });

  purchaseForm: FormGroup = this.fb.group({
    employeeName: ['', Validators.required],
    vendor: ['', Validators.required],
    total: [0, [Validators.required, Validators.min(0.01)]],
    justification: ['', Validators.required],
    attachmentPath: ['']
  });

  ngOnInit() {
    this.loadSettings();
    this.loadData();
  }

  get items(): FormArray<FormGroup> {
    return this.reportForm.get('items') as FormArray<FormGroup>;
  }

  addItem(existing?: ExpenseItem) {
    this.items.push(this.createItemGroup(existing));
  }

  removeItem(index: number) {
    if (this.items.length === 1) {
      return;
    }
    this.items.removeAt(index);
  }

  editReport(report: ExpenseReport) {
    this.editingReportId = report.id;
    this.reportForm.patchValue({
      title: report.title,
      employeeName: report.employeeName,
      currency: report.currency
    });
    this.items.clear();
    report.items.forEach(item => this.addItem(item));
  }

  resetReportForm() {
    this.editingReportId = null;
    this.reportForm.reset({ title: '', employeeName: '', currency: 'USD' });
    this.items.clear();
    this.addItem();
  }

  editPurchase(request: PurchaseRequest) {
    this.editingPurchaseId = request.id;
    this.purchaseForm.patchValue({
      employeeName: request.employeeName,
      vendor: request.vendor,
      total: request.total,
      justification: request.justification,
      attachmentPath: request.attachmentPath ?? ''
    });
  }

  resetPurchaseForm() {
    this.editingPurchaseId = null;
    this.purchaseForm.reset({ employeeName: '', vendor: '', total: 0, justification: '', attachmentPath: '' });
  }

  submitReport() {
    if (this.reportForm.invalid) {
      this.reportForm.markAllAsTouched();
      return;
    }
    const payload = this.buildReportPayload();
    const request$ = this.editingReportId
      ? this.api.updateExpenseReport(this.editingReportId, payload)
      : this.api.createExpenseReport(payload);

    request$.subscribe({
      next: () => {
        this.notifications.success('Expense report saved');
        this.resetReportForm();
        this.loadReports();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  submitPurchase() {
    if (this.purchaseForm.invalid) {
      this.purchaseForm.markAllAsTouched();
      return;
    }
    const raw = this.purchaseForm.value;
    const payload: PurchaseRequestRequest = {
      employeeName: raw.employeeName!,
      vendor: raw.vendor!,
      total: Number(raw.total),
      justification: raw.justification!,
      attachmentPath: raw.attachmentPath || undefined
    };
    const request$ = this.editingPurchaseId
      ? this.api.updatePurchaseRequest(this.editingPurchaseId, payload)
      : this.api.createPurchaseRequest(payload);

    request$.subscribe({
      next: () => {
        this.notifications.success('Purchase request saved');
        this.resetPurchaseForm();
        this.loadPurchases();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  sendReport(report: ExpenseReport) {
    this.api.submitExpenseReport(report.id).subscribe({
      next: () => {
        this.notifications.success('Report submitted');
        this.loadReports();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  sendPurchase(request: PurchaseRequest) {
    this.api.submitPurchaseRequest(request.id).subscribe({
      next: () => {
        this.notifications.success('Purchase request submitted');
        this.loadPurchases();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  uploadReceipt(event: Event, index: number) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      return;
    }
    const file = input.files[0];
    this.api.uploadReceipt(file).subscribe({
      next: (response) => {
        this.items.at(index).patchValue({ attachmentPath: response.path });
        this.notifications.success('Receipt uploaded');
      },
      error: err => this.notifications.error(err.message)
    });
  }

  private buildReportPayload(): ExpenseReportRequest {
    const raw = this.reportForm.value;
    const items = (raw.items ?? []).map((item: Partial<ExpenseItem>) => ({
      ...item,
      amount: Number(item.amount),
      txDate: item.txDate
    })) as ExpenseItem[];
    return {
      title: raw.title!,
      employeeName: raw.employeeName!,
      currency: raw.currency!,
      items
    };
  }

  private loadSettings() {
    this.api.getSettings().subscribe({
      next: settings => {
        this.categories = settings.categories;
        this.costCenters = settings.costCenters;
      },
      error: err => this.notifications.error(err.message)
    });
  }

  private loadData() {
    this.loadReports();
    this.loadPurchases();
  }

  private loadReports() {
    this.api.getExpenseReports({ size: 50 }).subscribe({
      next: (response) => (this.reports = response.content),
      error: err => this.notifications.error(err.message)
    });
  }

  private loadPurchases() {
    this.api.getPurchaseRequests({ size: 50 }).subscribe({
      next: (response) => (this.purchases = response.content),
      error: err => this.notifications.error(err.message)
    });
  }

  private createItemGroup(item?: ExpenseItem) {
    return this.fb.group({
      txDate: [item?.txDate ?? '', Validators.required],
      amount: [item?.amount ?? 0, [Validators.required, Validators.min(0.01)]],
      currency: [item?.currency ?? this.reportForm?.get('currency')?.value ?? 'USD', [Validators.required, Validators.pattern(/^[A-Z]{3}$/)]],
      category: [item?.category ?? '', Validators.required],
      costCenter: [item?.costCenter ?? '', Validators.required],
      attachmentPath: [item?.attachmentPath ?? ''],
      description: [item?.description ?? '']
    });
  }
}
