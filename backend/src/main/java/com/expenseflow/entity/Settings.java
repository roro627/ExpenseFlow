package com.expenseflow.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "settings")
public class Settings {

    @Id
    private Long id = 1L;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "settings_categories", joinColumns = @JoinColumn(name = "settings_id"))
    @Column(name = "category")
    private List<String> categories = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "settings_cost_centers", joinColumns = @JoinColumn(name = "settings_id"))
    @Column(name = "cost_center")
    private List<String> costCenters = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCostCenters() {
        return costCenters;
    }

    public void setCostCenters(List<String> costCenters) {
        this.costCenters = costCenters;
    }
}
