package com.expenseflow.dto;

import com.expenseflow.entity.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record ManagerDecisionRequest(
        @NotNull RequestStatus status,
        String comment
) {
}
