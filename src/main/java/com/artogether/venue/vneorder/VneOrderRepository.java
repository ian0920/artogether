package com.artogether.venue.vneorder;

import com.artogether.event.evt_order.EvtOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VneOrderRepository extends JpaRepository<VneOrder, Integer> {

    @Query(value = "SELECT * FROM vne_order WHERE vne_id = ?1 AND end_date >  ?2 ", nativeQuery = true)
    List<VneOrder> findUnclosedOrders (Integer vneId, LocalDate today);

    List<VneOrder> findByVenue_Id(Integer vneId);

    List<VneOrder> findByMember_Id(Integer memberId);
    //@Modifying是指操作不會返回具體的實體集合
    @Modifying
    @Transactional
    @Query(value = """
        CREATE TEMPORARY TABLE temp_schedule AS
        WITH RECURSIVE 
        hours AS (
            SELECT 0 AS hour_value
            UNION ALL
            SELECT hour_value + 1
            FROM hours
            WHERE hour_value + 1 < 24
        ),
        dates (v_date) AS (
            SELECT CURDATE() + INTERVAL 1 DAY
            UNION ALL
            SELECT v_date + INTERVAL 1 DAY
            FROM dates
            WHERE v_date + INTERVAL 1 DAY <= ADDDATE(CURDATE(), INTERVAL 30 DAY)
        )
        SELECT 
            d.v_date, 
            DAYNAME(d.v_date) AS weekday,
            h.hour_value AS hour,
            t.work_hours,
            t.effective_time
        FROM dates d
        CROSS JOIN hours h
        JOIN tslot_schedule t
        ON DAYNAME(d.v_date) = t.weekday
        WHERE t.tslot_id = 1
          AND SUBSTRING(t.work_hours, h.hour_value + 1, 1) = '1'
        ORDER BY d.v_date, h.hour_value
        """, nativeQuery = true)
    void createTempSchedule();

    // 歐：商家ID及開始結束日期
//    @Query("SELECT v FROM VneOrder v " + "WHERE (:businessId IS NULL OR v.venue.id IN :businessId) " + "AND (:startDate IS NULL OR v.orderDate >= :startDate) " + "AND (:endDate IS NULL OR v.orderDate <= :endDate) ")
//    List<VneOrder> findAllByVenue_idInAndOrderDateBetween(List<Integer> businessId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT v FROM VneOrder v " +
            "WHERE (:businessId IS NULL OR v.venue.id IN :businessId) " +
            "AND (:startDate IS NULL OR v.orderDate >= :startDate) " +
            "AND (:endDate IS NULL OR v.orderDate <= :endDate) ")
    List<VneOrder> findAllByVenue_idInAndOrderDateBetween(
            @Param("businessId") List<Integer> businessId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}