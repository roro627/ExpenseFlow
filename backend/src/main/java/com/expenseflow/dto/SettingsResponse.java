package com.expenseflow.dto;

import java.util.List;

public record SettingsResponse(
        List<String> categories,
        List<String> costCenters
) {
}
