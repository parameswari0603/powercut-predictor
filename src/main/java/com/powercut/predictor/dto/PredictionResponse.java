package com.powercut.predictor.dto;

import com.powercut.predictor.model.OutageRisk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponse {
    private String area;
    private OutageRisk riskLevel;
    private double riskScore;
    private String predictedWindow;
    private String reason;
    private double currentTemperature;
    private int totalReportsAnalyzed;
}
