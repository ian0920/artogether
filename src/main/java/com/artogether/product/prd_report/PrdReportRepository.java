package com.artogether.product.prd_report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdReportRepository extends JpaRepository<PrdReport, Integer> {
}
