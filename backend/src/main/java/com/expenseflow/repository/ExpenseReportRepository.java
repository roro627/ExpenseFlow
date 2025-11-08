package com.expenseflow.repository;

import com.expenseflow.entity.ExpenseReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long>, JpaSpecificationExecutor<ExpenseReport> {
}
