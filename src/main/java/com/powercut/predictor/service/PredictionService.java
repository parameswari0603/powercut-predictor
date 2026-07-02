package com.powercut.predictor.service;

import com.powercut.predictor.dto.PredictionResponse;
import com.powercut.predictor.exception.AreaNotFoundException;
import com.powercut.predictor.model.Area;
import com.powercut.predictor.model.OutageReport;
import com.powercut.predictor.model.OutageRisk;
import com.powercut.predictor.repository.AreaRepository;
import com.powercut.predictor.repository.OutageReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PredictionService {

    @Autowired
    private OutageReportRepository reportRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private WeatherService weatherService;

    public PredictionResponse predict(String areaName) {

        Area area = areaRepository
                .findByNameIgnoreCase(areaName)
                .orElseThrow(() ->
                        new AreaNotFoundException(areaName));

        List<OutageReport> reports = reportRepository
                .findByAreaNameIgnoreCaseAndDeletedFalse(
                        areaName);

        if (reports.isEmpty()) {
            return PredictionResponse.builder()
                    .area(areaName)
                    .riskLevel(OutageRisk.LOW)
                    .riskScore(0)
                    .predictedWindow("No data yet")
                    .reason("No reports for this area yet.")
                    .totalReportsAnalyzed(0)
                    .build();
        }

        int currentHour =
                LocalDateTime.now().getHour();
        String currentDayType =
                isWeekday() ? "WEEKDAY" : "WEEKEND";
        double currentTemp =
                weatherService.getCurrentTemperature(
                        "Chennai");
        int total = reports.size();

        long sameHourCount = reports.stream()
                .filter(r -> r.getReportedHour() != null)
                .filter(r -> Math.abs(
                        r.getReportedHour() - currentHour) <= 2)
                .count();

        long sameDayCount = reports.stream()
                .filter(r -> currentDayType
                        .equals(r.getDayType()))
                .count();

        long sameTempCount = reports.stream()
                .filter(r -> r.getTemperatureAtTime() != null)
                .filter(r -> Math.abs(
                        r.getTemperatureAtTime()
                                - currentTemp) <= 3)
                .count();

        double hourScore =
                (double) sameHourCount / total * 100;
        double dayScore =
                (double) sameDayCount / total * 100;
        double tempScore =
                (double) sameTempCount / total * 100;
        double finalScore =
                (hourScore * 0.4)
                        + (dayScore * 0.3)
                        + (tempScore * 0.3);

        OutageRisk risk;
        if      (finalScore >= 70)
            risk = OutageRisk.VERY_HIGH;
        else if (finalScore >= 45)
            risk = OutageRisk.HIGH;
        else if (finalScore >= 25)
            risk = OutageRisk.MEDIUM;
        else
            risk = OutageRisk.LOW;

        String reason = String.format(
                "%d of %d past outages in %s happened "
                        + "around %d:00 on a %s when temp "
                        + "was near %.1f C.",
                sameHourCount, total, areaName,
                currentHour, currentDayType.toLowerCase(),
                currentTemp);

        return PredictionResponse.builder()
                .area(areaName)
                .riskLevel(risk)
                .riskScore(Math.round(
                        finalScore * 10.0) / 10.0)
                .predictedWindow(currentHour + ":00 - "
                        + (currentHour + 2) + ":00")
                .reason(reason)
                .currentTemperature(currentTemp)
                .totalReportsAnalyzed(total)
                .build();
    }

    private boolean isWeekday() {
        int day = LocalDateTime.now()
                .getDayOfWeek().getValue();
        return day >= 1 && day <= 5;
    }
}