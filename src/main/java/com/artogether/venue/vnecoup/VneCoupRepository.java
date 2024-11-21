package com.artogether.venue.vnecoup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VneCoupRepository extends JpaRepository<VneCoup, Integer> {
}
