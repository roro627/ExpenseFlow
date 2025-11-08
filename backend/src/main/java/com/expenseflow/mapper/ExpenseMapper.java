package com.expenseflow.mapper;

import com.expenseflow.dto.ExpenseItemRequest;
import com.expenseflow.dto.ExpenseItemResponse;
import com.expenseflow.dto.ExpenseReportResponse;
import com.expenseflow.dto.PurchaseRequestResponse;
import com.expenseflow.entity.ExpenseItem;
import com.expenseflow.entity.ExpenseReport;
import com.expenseflow.entity.PurchaseRequest;

import java.math.BigDecimal;
import java.util.List;

public final class ExpenseMapper {

    private ExpenseMapper() {
    }

    public static ExpenseItem toEntity(ExpenseItemRequest request) {
        ExpenseItem item = new ExpenseItem();
        item.setTxDate(request.txDate());
        item.setAmount(request.amount());
        item.setCurrency(request.currency());
        item.setCategory(request.category());
        item.setCostCenter(request.costCenter());
        item.setAttachmentPath(request.attachmentPath());
        item.setDescription(request.description());
        return item;
    }

    public static ExpenseItemResponse toResponse(ExpenseItem entity) {
        return new ExpenseItemResponse(
                entity.getId(),
                entity.getTxDate(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getCategory(),
                entity.getCostCenter(),
                entity.getAttachmentPath(),
                entity.getDescription()
        );
    }

    public static ExpenseReportResponse toResponse(ExpenseReport report) {
        List<ExpenseItemResponse> items = report.getItems().stream()
                .map(ExpenseMapper::toResponse)
                .toList();
        return new ExpenseReportResponse(
                report.getId(),
                report.getTitle(),
                report.getEmployeeName(),
                report.getCurrency(),
                report.getTotal(),
                report.getStatus(),
                report.getSubmittedAt(),
                report.getPaidAt(),
                report.getManagerComment(),
                report.getFinanceNote(),
                report.getCreatedAt(),
                report.getUpdatedAt(),
                items
        );
    }

    public static PurchaseRequestResponse toResponse(PurchaseRequest request) {
        return new PurchaseRequestResponse(
                request.getId(),
                request.getEmployeeName(),
                request.getVendor(),
                request.getTotal(),
                request.getJustification(),
                request.getAttachmentPath(),
                request.getStatus(),
                request.getSubmittedAt(),
                request.getPaidAt(),
                request.getManagerComment(),
                request.getFinanceNote(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }

    public static BigDecimal calculateTotal(List<ExpenseItemRequest> items) {
        return items.stream()
                .map(ExpenseItemRequest::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
