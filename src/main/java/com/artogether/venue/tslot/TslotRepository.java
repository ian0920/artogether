package com.artogether.venue.tslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TslotRepository extends JpaRepository<Tslot, Integer> {

    /* 以下方法缺少Query查詢語句，請補上後再解開註解 */
//    List<Tslot> findByVneIdAndDayOfWeek(Integer vneId, DayOfWeek dayOfWeek );
//    Boolean existsByVneIdAndDayOfWeek(Integer vneId, DayOfWeek dayOfWeek );
    Boolean existsByVenueId(Integer vneId);
    //原生sql語句
    @Query(value = "SELECT * FROM tslot WHERE effective_time <= ?1 ORDER BY effective_time DESC LIMIT 1",nativeQuery = true)
    Optional<Tslot> getNearestPastRecord(LocalDateTime submissionTime);
}
