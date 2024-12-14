package com.artogether.venue.tslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TslotRepository extends JpaRepository<Tslot, Integer> {

    @Query(value = "SELECT * FROM tslot WHERE vne_id = ?1 AND effective_time <= ?2 ORDER BY effective_time DESC LIMIT 1",nativeQuery = true)
    Optional<Tslot> getNearestPastRecord(Integer vneId, LocalDateTime submissionTime);

    //取出可預約期限內所有日期列表
    @Query(value = """
                     WITH RECURSIVE dates (v_date) AS (
                                SELECT CURDATE() + INTERVAL 1 DAY
                                UNION ALL
                                SELECT v_date + INTERVAL 1 DAY
                                FROM dates
                                WHERE v_date + INTERVAL 1 DAY <= ADDDATE(CURDATE(), INTERVAL ?1 DAY)
                            )
                            SELECT
                                v_date
                            FROM dates
                    """,nativeQuery = true)
    List<Date> getAvailableDates(Integer availableDays);
}
