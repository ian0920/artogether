package com.artogether.venue.venue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {
    List<Venue> findByBusinessMember_Id(int bizId);

    @Query(value = """
                     WITH RECURSIVE dates (v_date) AS (
                                SELECT CURDATE() + INTERVAL 1 DAY
                                UNION ALL
                                SELECT v_date + INTERVAL 1 DAY
                                FROM dates
                                WHERE v_date + INTERVAL 1 DAY <= ADDDATE(CURDATE(), INTERVAL 30 DAY)
                            )
                            SELECT
                                v_date,
                                WEEKDAY(v_date)+1 AS weekday
                            FROM dates
                    """,nativeQuery = true)
    List<LocalDate> getAvailableDates(Integer availableDays);

}
