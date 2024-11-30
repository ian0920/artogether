package com.artogether.product.prd_report;

import java.util.List;

public interface PrdReportService {
    
    int addReport(PrdReport prdReport);

    int updateReport(PrdReport prdReport);

    int deleteReport(Integer id);

    PrdReport findReportById(Integer id);

    List<PrdReport> getAllReports();

    int submitReport(PrdReport prdReport);

    List<PrdReport> findReportsByMember(Integer memberId);

    boolean hasReportedProduct(Integer memberId, Integer prdId);
}

