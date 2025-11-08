package com.expenseflow.dto;

import com.expenseflow.entity.RequestStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ExpenseReportResponse(
        Long id,
        String title,
        String employeeName,
        String currency,
        BigDecimal total,
        RequestStatus status,
        Instant submittedAt,
        Instant paidAt,
        String managerComment,
        String financeNote,
        Instant createdAt,
        Instant updatedAt,
        List<ExpenseItemResponse> items
) {
}
