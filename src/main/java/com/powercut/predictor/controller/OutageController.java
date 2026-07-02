package com.powercut.predictor.controller;

import com.powercut.predictor.dto.OutageReportRequest;
import com.powercut.predictor.model.OutageReport;
import com.powercut.predictor.service.OutageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class OutageController {

    @Autowired
    private OutageService outageService;

    @PostMapping
    public ResponseEntity<OutageReport> submitReport(
            @Valid @RequestBody
            OutageReportRequest request) {
        OutageReport saved =
                outageService.reportOutage(request);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<OutageReport>> getReports(
            @RequestParam String area) {
        return ResponseEntity.ok(
                outageService.getReportsForArea(area));
    }
}
