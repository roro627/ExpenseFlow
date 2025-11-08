package com.expenseflow.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseItemResponse(
        Long id,
        LocalDate txDate,
        BigDecimal amount,
        String currency,
        String category,
        String costCenter,
        String attachmentPath,
        String description
) {
}
