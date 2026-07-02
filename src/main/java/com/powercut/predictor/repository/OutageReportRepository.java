package com.powercut.predictor.repository;

import com.powercut.predictor.model.OutageReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OutageReportRepository
        extends JpaRepository<OutageReport, Long> {

    List<OutageReport> findByAreaNameIgnoreCaseAndDeletedFalse(
            String areaName);

    @Query("SELECT o FROM OutageReport o " +
            "WHERE o.area.id = :areaId " +
            "AND o.deleted = false " +
            "ORDER BY o.startTime DESC")
    List<OutageReport> findRecentByArea(
            @Param("areaId") Long areaId);
}