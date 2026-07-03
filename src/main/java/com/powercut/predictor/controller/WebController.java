package com.powercut.predictor.controller;

import com.powercut.predictor.dto.OutageReportRequest;
import com.powercut.predictor.dto.PredictionResponse;
import com.powercut.predictor.repository.AreaRepository;
import com.powercut.predictor.service.OutageService;
import com.powercut.predictor.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@Controller
public class WebController {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private OutageService outageService;

    @Autowired
    private AreaRepository areaRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("areas",
                areaRepository.findAll());
        return "index";
    }

    @GetMapping("/predict")
    public String predict(
            @RequestParam String area,
            Model model) {
        try {
            PredictionResponse prediction =
                    predictionService.predict(area);
            model.addAttribute("prediction",
                    prediction);
        } catch (Exception e) {
            model.addAttribute("error",
                    e.getMessage());
        }
        model.addAttribute("areas",
                areaRepository.findAll());
        return "index";
    }

    @GetMapping("/report")
    public String reportForm() {
        return "report-form";
    }

    @PostMapping("/report")
    public String submitReport(
            @RequestParam String areaName,
            @RequestParam String startTime,
            @RequestParam(required = false)
            String endTime,
            Model model) {
        try {
            OutageReportRequest request =
                    new OutageReportRequest();
            request.setAreaName(areaName);
            request.setStartTime(
                    LocalDateTime.parse(startTime));
            if (endTime != null
                    && !endTime.isEmpty()) {
                request.setEndTime(
                        LocalDateTime.parse(endTime));
            }
            outageService.reportOutage(request);
            model.addAttribute("success",
                    "Report submitted for " + areaName);
        } catch (Exception e) {
            model.addAttribute("error",
                    "Error: " + e.getMessage());
        }
        return "report-form";
    }

    @GetMapping("/heatmap")
    public String heatmap() {
        return "heatmap";
    }

    @GetMapping("/patterns")
    public String patterns() {
        return "patterns";
    }


}
