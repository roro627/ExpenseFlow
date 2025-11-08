package com.expenseflow.controller;

import com.expenseflow.dto.ExpenseReportRequest;
import com.expenseflow.dto.ExpenseReportResponse;
import com.expenseflow.dto.FinanceActionRequest;
import com.expenseflow.dto.ManagerDecisionRequest;
import com.expenseflow.dto.PageResponse;
import com.expenseflow.entity.RequestStatus;
import com.expenseflow.service.ExpenseReportService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ExpenseReportController {

    private final ExpenseReportService service;

    public ExpenseReportController(ExpenseReportService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<ExpenseReportResponse> list(@RequestParam(required = false) RequestStatus status,
                                                    @RequestParam(required = false) String employee,
                                                    @PageableDefault Pageable pageable) {
        return service.list(status, employee, pageable);
    }

    @GetMapping("/{id}")
    public ExpenseReportResponse find(@PathVariable Long id) {
        return service.find(id);
    }

    @PostMapping
    public ExpenseReportResponse create(@Valid @RequestBody ExpenseReportRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ExpenseReportResponse update(@PathVariable Long id, @Valid @RequestBody ExpenseReportRequest request) {
        return service.update(id, request);
    }

    @PostMapping("/{id}/submit")
    public ExpenseReportResponse submit(@PathVariable Long id) {
        return service.submit(id);
    }

    @PostMapping("/{id}/manager")
    public ExpenseReportResponse managerDecision(@PathVariable Long id, @Valid @RequestBody ManagerDecisionRequest request) {
        return service.managerDecision(id, request);
    }

    @PostMapping("/{id}/finance/pay")
    public ExpenseReportResponse pay(@PathVariable Long id, @Valid @RequestBody FinanceActionRequest request) {
        return service.markPaid(id, request);
    }
}
