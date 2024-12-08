package com.artogether.venue.vneprice;

import com.artogether.venue.tslot.Tslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VnePriceRepository extends JpaRepository<VnePrice, Long> {

    //原生sql語句
    @Query(value = "SELECT * FROM vne_price WHERE vne_id = ?1 AND effective_time <= ?1 ORDER BY effective_time DESC LIMIT 1",nativeQuery = true)
    Optional<VnePrice> getNearestPastRecord(Integer vneId, LocalDateTime submissionTime);
}
