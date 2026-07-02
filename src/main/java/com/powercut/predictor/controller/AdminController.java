package com.powercut.predictor.controller;

import com.powercut.predictor.model.OutageReport;
import com.powercut.predictor.repository.AreaRepository;
import com.powercut.predictor.repository.OutageReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OutageReportRepository reportRepository;

    @Autowired
    private AreaRepository areaRepository;

    @GetMapping
    public String dashboard(Model model) {

        List<OutageReport> allReports =
                reportRepository.findAll();

        long reportsToday = allReports.stream()
                .filter(r -> r.getCreatedAt() != null
                        && r.getCreatedAt().isAfter(
                        LocalDateTime.now()
                                .withHour(0)
                                .withMinute(0)))
                .count();

        long activeAreas = allReports.stream()
                .map(r -> r.getArea().getName())
                .distinct()
                .count();

        List<OutageReport> recentReports =
                allReports.stream()
                        .filter(r -> !r.isDeleted())
                        .sorted((a, b) -> {
                            if (a.getCreatedAt() == null)
                                return 1;
                            if (b.getCreatedAt() == null)
                                return -1;
                            return b.getCreatedAt()
                                    .compareTo(a.getCreatedAt());
                        })
                        .limit(20)
                        .toList();

        model.addAttribute("totalReports",
                allReports.size());
        model.addAttribute("totalAreas",
                areaRepository.count());
        model.addAttribute("reportsToday",
                reportsToday);
        model.addAttribute("activeAreas",
                activeAreas);
        model.addAttribute("recentReports",
                recentReports);

        return "admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteReport(
            @PathVariable Long id) {
        reportRepository.findById(id)
                .ifPresent(report -> {
                    report.setDeleted(true);
                    reportRepository.save(report);
                });
        return "redirect:/admin";
    }
}
