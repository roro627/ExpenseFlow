package com.expenseflow.service;

import com.expenseflow.dto.SettingsResponse;
import com.expenseflow.dto.SettingsUpdateRequest;
import com.expenseflow.entity.Settings;
import com.expenseflow.repository.SettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SettingsService {

    private final SettingsRepository repository;

    public SettingsService(SettingsRepository repository) {
        this.repository = repository;
    }

    public SettingsResponse get() {
        Settings settings = repository.findById(1L).orElseGet(this::createDefaults);
        return new SettingsResponse(settings.getCategories(), settings.getCostCenters());
    }

    public SettingsResponse update(SettingsUpdateRequest request) {
        Settings settings = repository.findById(1L).orElseGet(this::createDefaults);
        settings.setCategories(new java.util.ArrayList<>(request.categories()));
        settings.setCostCenters(new java.util.ArrayList<>(request.costCenters()));
        repository.save(settings);
        return new SettingsResponse(settings.getCategories(), settings.getCostCenters());
    }

    private Settings createDefaults() {
        Settings defaults = new Settings();
        defaults.setCategories(new java.util.ArrayList<>(java.util.List.of("Travel", "Meals", "Supplies")));
        defaults.setCostCenters(new java.util.ArrayList<>(java.util.List.of("OPS", "HR", "ENG")));
        return repository.save(defaults);
    }
}
