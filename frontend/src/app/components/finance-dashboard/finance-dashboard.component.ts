import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { ExpenseReport, PurchaseRequest, SettingsPayload } from '../../models/api.models';
import { NotificationService } from '../../core/notification.service';

@Component({
  selector: 'app-finance-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, NgForOf, NgIf, CurrencyPipe, DatePipe],
  templateUrl: './finance-dashboard.component.html'
})
export class FinanceDashboardComponent implements OnInit {
  private readonly api = inject(ApiService);
  private readonly notifications = inject(NotificationService);

  reports: ExpenseReport[] = [];
  purchases: PurchaseRequest[] = [];
  categories: string[] = [];
  costCenters: string[] = [];
  newCategory = '';
  newCostCenter = '';

  ngOnInit() {
    this.loadData();
    this.loadSettings();
  }

  markReportPaid(report: ExpenseReport) {
    const note = window.prompt('Payment note');
    if (!note) {
      return;
    }
    this.api.markReportPaid(report.id, note).subscribe({
      next: () => {
        this.notifications.success('Report marked as paid');
        this.loadReports();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  markPurchasePaid(request: PurchaseRequest) {
    const note = window.prompt('Payment note');
    if (!note) {
      return;
    }
    this.api.markPurchasePaid(request.id, note).subscribe({
      next: () => {
        this.notifications.success('Purchase marked as paid');
        this.loadPurchases();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  addCategory() {
    if (this.newCategory && !this.categories.includes(this.newCategory)) {
      this.categories = [...this.categories, this.newCategory];
      this.newCategory = '';
    }
  }

  removeCategory(cat: string) {
    this.categories = this.categories.filter(c => c !== cat);
  }

  addCostCenter() {
    if (this.newCostCenter && !this.costCenters.includes(this.newCostCenter)) {
      this.costCenters = [...this.costCenters, this.newCostCenter];
      this.newCostCenter = '';
    }
  }

  removeCostCenter(cc: string) {
    this.costCenters = this.costCenters.filter(c => c !== cc);
  }

  saveSettings() {
    const payload: SettingsPayload = { categories: this.categories, costCenters: this.costCenters };
    this.api.updateSettings(payload).subscribe({
      next: settings => {
        this.categories = settings.categories;
        this.costCenters = settings.costCenters;
        this.notifications.success('Settings saved');
      },
      error: err => this.notifications.error(err.message)
    });
  }

  exportCsv() {
    this.api.downloadReportsCsv().subscribe({
      next: blob => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'expense-reports.csv';
        link.click();
        window.URL.revokeObjectURL(url);
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
      next: response => (this.reports = response.content),
      error: err => this.notifications.error(err.message)
    });
  }

  private loadPurchases() {
    this.api.getPurchaseRequests({ size: 50 }).subscribe({
      next: response => (this.purchases = response.content),
      error: err => this.notifications.error(err.message)
    });
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
}
