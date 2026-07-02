package com.powercut.predictor.controller;

import com.powercut.predictor.dto.AreaRiskSummary;
import com.powercut.predictor.dto.PredictionResponse;
import com.powercut.predictor.model.Area;
import com.powercut.predictor.repository.AreaRepository;
import com.powercut.predictor.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;
import com.powercut.predictor.model.OutageReport;
import com.powercut.predictor.repository.OutageReportRepository;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prediction")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private OutageReportRepository reportRepository;


    @GetMapping("/{area}")
    public PredictionResponse getPrediction(
            @PathVariable String area) {
        return predictionService.predict(area);
    }

    @GetMapping("/all/risk-summary")
    public List<AreaRiskSummary> getAllRiskSummary() {
        List<Area> areas = areaRepository.findAll();
        return areas.stream().map(area -> {
            PredictionResponse prediction =
                    predictionService.predict(area.getName());
            return new AreaRiskSummary(
                    area.getName(),
                    prediction.getRiskLevel(),
                    prediction.getRiskScore(),
                    area.getLatitude() != null
                            ? area.getLatitude() : 13.0827,
                    area.getLongitude() != null
                            ? area.getLongitude() : 80.2707
            );
        }).collect(Collectors.toList());
    }
    @GetMapping("/patterns/{area}")
    public Map<String, Object> getPatterns(
            @PathVariable String area) {

        List<OutageReport> reports = reportRepository
                .findByAreaNameIgnoreCaseAndDeletedFalse(area);

        // Count by hour
        Map<Integer, Long> byHour = reports.stream()
                .filter(r -> r.getReportedHour() != null)
                .collect(Collectors.groupingBy(
                        OutageReport::getReportedHour,
                        Collectors.counting()));

        // Count by day type
        Map<String, Long> byDayType = reports.stream()
                .filter(r -> r.getDayType() != null)
                .collect(Collectors.groupingBy(
                        OutageReport::getDayType,
                        Collectors.counting()));

        // Count by temperature range
        Map<String, Long> byTempRange = reports.stream()
                .filter(r -> r.getTemperatureAtTime() != null)
                .collect(Collectors.groupingBy(r -> {
                    double t = r.getTemperatureAtTime();
                    if (t < 30) return "Below 30C";
                    if (t < 33) return "30-33C";
                    if (t < 36) return "33-36C";
                    return "Above 36C";
                }, Collectors.counting()));

        return Map.of(
                "area", area,
                "totalReports", reports.size(),
                "byHour", byHour,
                "byDayType", byDayType,
                "byTempRange", byTempRange
        );
    }

}