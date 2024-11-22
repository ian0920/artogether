package com.artogether.venue.tslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TslotRepository extends JpaRepository<Tslot, Integer> {

    /* 以下方法缺少Query查詢語句，請補上後再解開註解 */
//    List<Tslot> findByVneIdAndDayOfWeek(Integer vneId, DayOfWeek dayOfWeek );
//    Boolean existsByVneIdAndDayOfWeek(Integer vneId, DayOfWeek dayOfWeek );
}
