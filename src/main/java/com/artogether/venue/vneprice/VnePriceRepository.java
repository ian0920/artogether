package com.artogether.venue.vneprice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VnePriceRepository extends JpaRepository<VnePrice, Long> {
}
