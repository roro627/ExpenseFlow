package com.expenseflow.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PurchaseRequestRequest(
        @NotBlank String employeeName,
        @NotBlank String vendor,
        @NotNull @DecimalMin("0.0") BigDecimal total,
        @NotBlank String justification,
        String attachmentPath
) {
}
