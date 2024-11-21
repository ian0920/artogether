package com.artogether.venue.tslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface TslotRepository extends JpaRepository<Tslot, Integer> {

    List<Tslot> findByVneIdAndDayOfWeek(Integer vneId, DayOfWeek dayOfWeek );
    Boolean existsByVneIdAndDayOfWeek(Integer vneId, DayOfWeek dayOfWeek );
}
