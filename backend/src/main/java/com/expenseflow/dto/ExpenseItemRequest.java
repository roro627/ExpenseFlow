package com.expenseflow.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseItemRequest(
        Long id,
        @NotNull LocalDate txDate,
        @NotNull @DecimalMin("0.0") BigDecimal amount,
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency,
        @NotBlank String category,
        @NotBlank String costCenter,
        String attachmentPath,
        String description
) {
}
