package com.expenseflow.spec;

import com.expenseflow.entity.ExpenseReport;
import com.expenseflow.entity.RequestStatus;
import org.springframework.data.jpa.domain.Specification;

public final class ExpenseReportSpecifications {

    private ExpenseReportSpecifications() {
    }

    public static Specification<ExpenseReport> withStatus(RequestStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<ExpenseReport> withEmployee(String employeeName) {
        if (employeeName == null || employeeName.isBlank()) {
            return null;
        }
        return (root, query, cb) -> cb.like(cb.lower(root.get("employeeName")), "%" + employeeName.toLowerCase() + "%");
    }

    public static Specification<ExpenseReport> compose(RequestStatus status, String employeeName) {
        Specification<ExpenseReport> spec = Specification.where(null);
        if (status != null) {
            spec = spec == null ? withStatus(status) : spec.and(withStatus(status));
        }
        if (employeeName != null && !employeeName.isBlank()) {
            Specification<ExpenseReport> employeeSpec = withEmployee(employeeName);
            spec = spec == null ? employeeSpec : spec.and(employeeSpec);
        }
        return spec;
    }
}
