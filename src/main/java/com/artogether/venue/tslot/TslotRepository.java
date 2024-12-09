package com.artogether.venue.tslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TslotRepository extends JpaRepository<Tslot, Integer> {

    //原生sql語句
    @Query(value = "SELECT * FROM tslot WHERE vne_id = ?1 AND effective_time <= ?2 ORDER BY effective_time DESC LIMIT 1",nativeQuery = true)
    Optional<Tslot> getNearestPastRecord(Integer vneId, LocalDateTime submissionTime);
}
