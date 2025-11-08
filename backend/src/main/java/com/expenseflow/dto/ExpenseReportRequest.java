package com.expenseflow.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record ExpenseReportRequest(
        @NotBlank String title,
        @NotBlank String employeeName,
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency,
        @NotEmpty List<@Valid ExpenseItemRequest> items
) {
}
