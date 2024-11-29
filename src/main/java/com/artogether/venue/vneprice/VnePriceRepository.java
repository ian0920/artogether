package com.artogether.venue.vneprice;

import com.artogether.venue.tslot.Tslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VnePriceRepository extends JpaRepository<VnePrice, Long> {
    Boolean existsByVenueId(Integer vneId);
    //原生sql語句
    @Query(value = "SELECT * FROM vne_price WHERE effective_time <= ?1 ORDER BY effective_time DESC LIMIT 1",nativeQuery = true)
    VnePrice getNearestPastRecord(LocalDateTime submissionTime);
}
