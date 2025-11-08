package com.expenseflow.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SettingsUpdateRequest(
        @NotEmpty List<String> categories,
        @NotEmpty List<String> costCenters
) {
}
