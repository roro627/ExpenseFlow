package com.expenseflow.service;

import com.expenseflow.dto.ExpenseItemRequest;
import com.expenseflow.dto.ExpenseReportRequest;
import com.expenseflow.dto.ExpenseReportResponse;
import com.expenseflow.dto.FinanceActionRequest;
import com.expenseflow.dto.ManagerDecisionRequest;
import com.expenseflow.dto.PageResponse;
import com.expenseflow.entity.ExpenseItem;
import com.expenseflow.entity.ExpenseReport;
import com.expenseflow.entity.RequestStatus;
import com.expenseflow.exception.BadRequestException;
import com.expenseflow.exception.NotFoundException;
import com.expenseflow.mapper.ExpenseMapper;
import com.expenseflow.repository.ExpenseReportRepository;
import com.expenseflow.spec.ExpenseReportSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class ExpenseReportService {

    private final ExpenseReportRepository repository;

    public ExpenseReportService(ExpenseReportRepository repository) {
        this.repository = repository;
    }

    public PageResponse<ExpenseReportResponse> list(RequestStatus status, String employeeName, Pageable pageable) {
        Specification<ExpenseReport> spec = ExpenseReportSpecifications.compose(status, employeeName);
        Page<ExpenseReport> page = repository.findAll(spec, pageable);
        List<ExpenseReportResponse> content = page.stream()
                .map(ExpenseMapper::toResponse)
                .toList();
        return new PageResponse<>(content, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
    }

    public ExpenseReportResponse find(Long id) {
        return ExpenseMapper.toResponse(load(id));
    }

    public ExpenseReportResponse create(ExpenseReportRequest request) {
        ExpenseReport report = new ExpenseReport();
        applyRequest(report, request);
        report.setStatus(RequestStatus.DRAFT);
        return ExpenseMapper.toResponse(repository.save(report));
    }

    public ExpenseReportResponse update(Long id, ExpenseReportRequest request) {
        ExpenseReport report = load(id);
        if (report.getStatus() != RequestStatus.DRAFT && report.getStatus() != RequestStatus.RETURNED) {
            throw new BadRequestException("Only draft or returned reports can be edited");
        }
        applyRequest(report, request);
        return ExpenseMapper.toResponse(repository.save(report));
    }

    public ExpenseReportResponse submit(Long id) {
        ExpenseReport report = load(id);
        if (report.getStatus() != RequestStatus.DRAFT && report.getStatus() != RequestStatus.RETURNED) {
            throw new BadRequestException("Only draft or returned reports can be submitted");
        }
        report.setStatus(RequestStatus.SUBMITTED);
        report.setSubmittedAt(Instant.now());
        return ExpenseMapper.toResponse(repository.save(report));
    }

    public ExpenseReportResponse managerDecision(Long id, ManagerDecisionRequest request) {
        ExpenseReport report = load(id);
        if (report.getStatus() != RequestStatus.SUBMITTED) {
            throw new BadRequestException("Only submitted reports can be processed by managers");
        }
        if (request.status() != RequestStatus.APPROVED && request.status() != RequestStatus.RETURNED) {
            throw new BadRequestException("Manager decision must be APPROVED or RETURNED");
        }
        if (request.status() == RequestStatus.RETURNED && (request.comment() == null || request.comment().isBlank())) {
            throw new BadRequestException("Comment is required when returning a report");
        }
        report.setStatus(request.status());
        report.setManagerComment(request.comment());
        if (request.status() == RequestStatus.APPROVED) {
            report.setSubmittedAt(report.getSubmittedAt() == null ? Instant.now() : report.getSubmittedAt());
        }
        return ExpenseMapper.toResponse(repository.save(report));
    }

    public ExpenseReportResponse markPaid(Long id, FinanceActionRequest request) {
        ExpenseReport report = load(id);
        if (report.getStatus() != RequestStatus.APPROVED) {
            throw new BadRequestException("Only approved reports can be marked as paid");
        }
        report.setStatus(RequestStatus.PAID);
        report.setFinanceNote(request.note());
        report.setPaidAt(Instant.now());
        return ExpenseMapper.toResponse(repository.save(report));
    }

    private void applyRequest(ExpenseReport report, ExpenseReportRequest request) {
        report.setTitle(request.title());
        report.setEmployeeName(request.employeeName());
        report.setCurrency(request.currency());

        List<ExpenseItem> items = request.items().stream()
                .map(this::toItem)
                .toList();

        report.getItems().clear();
        for (ExpenseItem item : items) {
            item.setExpenseReport(report);
            report.getItems().add(item);
        }
        report.setTotal(ExpenseMapper.calculateTotal(request.items()));
    }

    private ExpenseItem toItem(ExpenseItemRequest request) {
        ExpenseItem item = ExpenseMapper.toEntity(request);
        if (item.getAmount() == null) {
            throw new BadRequestException("Amount is required");
        }
        return item;
    }

    private ExpenseReport load(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense report %d not found".formatted(id)));
    }
}
