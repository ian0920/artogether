package com.artogether.venue.vneorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VneOrderRepository extends JpaRepository<VneOrder, Integer> {

    @Query(value = "SELECT * FROM vne_order WHERE vne_id = ?1 AND end_day >  ?2 ", nativeQuery = true)
    List<VneOrder> findUnclosedOrders (Integer vneId, LocalDate today);

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
}