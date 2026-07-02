package com.powercut.predictor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.persistence.Index;
@Entity
@Table(name = "outage_reports", indexes = {
        @Index(name = "idx_area_id",
                columnList = "area_id"),
        @Index(name = "idx_reported_hour",
                columnList = "reportedHour"),
        @Index(name = "idx_deleted",
                columnList = "deleted"),
        @Index(name = "idx_area_hour",
                columnList = "area_id, reportedHour")
})
@Data
@NoArgsConstructor
public class OutageReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "area_id",
            referencedColumnName = "id",
            nullable = false)
    @JsonIgnoreProperties("reports")
    private Area area;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double temperatureAtTime;
    private String dayType;
    private Integer reportedHour;
    private Integer reportedMonth;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
