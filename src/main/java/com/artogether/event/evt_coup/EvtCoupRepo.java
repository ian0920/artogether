package com.artogether.event.evt_coup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvtCoupRepo extends JpaRepository<EvtCoup, Integer> {
}
