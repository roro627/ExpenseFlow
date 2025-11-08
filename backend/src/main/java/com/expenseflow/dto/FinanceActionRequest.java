package com.expenseflow.dto;

import jakarta.validation.constraints.NotBlank;

public record FinanceActionRequest(
        @NotBlank String note
) {
}
