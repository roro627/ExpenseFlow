package com.expenseflow.service;

import com.expenseflow.entity.ExpenseReport;
import com.expenseflow.repository.ExpenseReportRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FinanceExportService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private final ExpenseReportRepository reportRepository;

    public FinanceExportService(ExpenseReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public byte[] exportReportsCsv() {
        List<ExpenseReport> reports = reportRepository.findAll();
        StringBuilder builder = new StringBuilder();
        builder.append("id,title,employee,currency,total,status,submittedAt,paidAt\n");
        reports.forEach(report -> builder.append(String.join(",",
                safe(report.getId()),
                safe(report.getTitle()),
                safe(report.getEmployeeName()),
                safe(report.getCurrency()),
                safe(report.getTotal()),
                safe(report.getStatus()),
                safe(report.getSubmittedAt()),
                safe(report.getPaidAt()))).append('\n'));
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String safe(Object value) {
        if (value == null) {
            return "\"\"";
        }
        String str = value instanceof java.time.Instant instant
                ? DATE_TIME_FORMATTER.format(instant)
                : value.toString();
        return "\"" + str.replace("\"", "\"\"") + "\"";
    }
}
