package com.powercut.predictor.service;

import com.powercut.predictor.dto.OutageReportRequest;
import com.powercut.predictor.exception.DuplicateReportException;
import com.powercut.predictor.model.Area;
import com.powercut.predictor.model.OutageReport;
import com.powercut.predictor.repository.AreaRepository;
import com.powercut.predictor.repository.OutageReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OutageService {

    @Autowired
    private OutageReportRepository reportRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private WeatherService weatherService;

    @Transactional
    public OutageReport reportOutage(
            OutageReportRequest request) {

        Area area = areaRepository
                .findByNameIgnoreCase(request.getAreaName())
                .orElseGet(() -> {
                    Area newArea = new Area();
                    newArea.setName(request.getAreaName());
                    newArea.setCity("Chennai");
                    return areaRepository.save(newArea);
                });

        List<OutageReport> recent = reportRepository
                .findByAreaNameIgnoreCaseAndDeletedFalse(
                        request.getAreaName());

        boolean isDuplicate = recent.stream()
                .anyMatch(r -> r.getCreatedAt() != null
                        && r.getCreatedAt().isAfter(
                        LocalDateTime.now()
                                .minusMinutes(15)));

        if (isDuplicate) {
            throw new DuplicateReportException(
                    request.getAreaName());
        }

        OutageReport report = new OutageReport();
        report.setArea(area);
        report.setStartTime(request.getStartTime());
        report.setEndTime(request.getEndTime());
        report.setTemperatureAtTime(
                weatherService.getCurrentTemperature(
                        "Chennai"));
        report.setDayType(
                isWeekday() ? "WEEKDAY" : "WEEKEND");
        report.setReportedHour(
                request.getStartTime().getHour());
        report.setReportedMonth(
                request.getStartTime().getMonthValue());

        return reportRepository.save(report);
    }

    public List<OutageReport> getReportsForArea(
            String areaName) {
        return reportRepository
                .findByAreaNameIgnoreCaseAndDeletedFalse(
                        areaName);
    }

    private boolean isWeekday() {
        int day = LocalDateTime.now()
                .getDayOfWeek().getValue();
        return day >= 1 && day <= 5;
    }
}
