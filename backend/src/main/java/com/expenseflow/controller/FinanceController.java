package com.expenseflow.controller;

import com.expenseflow.service.FinanceExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceExportService exportService;

    public FinanceController(FinanceExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/export/reports")
    public ResponseEntity<byte[]> downloadReports() {
        byte[] csv = exportService.exportReportsCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expense-reports.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
