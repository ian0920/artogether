package com.artogether.venue.vneorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VneOrderRepository extends JpaRepository<VneOrder, Integer> {
}
