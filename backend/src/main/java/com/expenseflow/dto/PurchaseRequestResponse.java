package com.expenseflow.dto;

import com.expenseflow.entity.RequestStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PurchaseRequestResponse(
        Long id,
        String employeeName,
        String vendor,
        BigDecimal total,
        String justification,
        String attachmentPath,
        RequestStatus status,
        Instant submittedAt,
        Instant paidAt,
        String managerComment,
        String financeNote,
        Instant createdAt,
        Instant updatedAt
) {
}
