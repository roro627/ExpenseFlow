import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe, NgForOf, NgIf } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { ExpenseReport, PurchaseRequest } from '../../models/api.models';
import { NotificationService } from '../../core/notification.service';

@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [CommonModule, NgForOf, NgIf, CurrencyPipe, DatePipe],
  templateUrl: './manager-dashboard.component.html'
})
export class ManagerDashboardComponent implements OnInit {
  private readonly api = inject(ApiService);
  private readonly notifications = inject(NotificationService);

  reports: ExpenseReport[] = [];
  purchases: PurchaseRequest[] = [];

  ngOnInit() {
    this.loadData();
  }

  approveReport(report: ExpenseReport) {
    this.api.managerDecisionOnReport(report.id, 'APPROVED').subscribe({
      next: () => {
        this.notifications.success('Report approved');
        this.loadReports();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  returnReport(report: ExpenseReport) {
    const comment = window.prompt('Return comment');
    if (!comment) {
      return;
    }
    this.api.managerDecisionOnReport(report.id, 'RETURNED', comment).subscribe({
      next: () => {
        this.notifications.success('Report returned');
        this.loadReports();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  approvePurchase(request: PurchaseRequest) {
    this.api.managerDecisionOnPurchase(request.id, 'APPROVED').subscribe({
      next: () => {
        this.notifications.success('Purchase approved');
        this.loadPurchases();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  returnPurchase(request: PurchaseRequest) {
    const comment = window.prompt('Return comment');
    if (!comment) {
      return;
    }
    this.api.managerDecisionOnPurchase(request.id, 'RETURNED', comment).subscribe({
      next: () => {
        this.notifications.success('Purchase returned');
        this.loadPurchases();
      },
      error: err => this.notifications.error(err.message)
    });
  }

  private loadData() {
    this.loadReports();
    this.loadPurchases();
  }

  private loadReports() {
    this.api.getExpenseReports({ status: 'SUBMITTED', size: 50 }).subscribe({
      next: response => (this.reports = response.content),
      error: err => this.notifications.error(err.message)
    });
  }

  private loadPurchases() {
    this.api.getPurchaseRequests({ status: 'SUBMITTED', size: 50 }).subscribe({
      next: response => (this.purchases = response.content),
      error: err => this.notifications.error(err.message)
    });
  }
}
