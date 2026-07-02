package com.powercut.predictor.dto;

import com.powercut.predictor.model.OutageRisk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaRiskSummary {
    private String areaName;
    private OutageRisk riskLevel;
    private double riskScore;
    private double latitude;
    private double longitude;
}
