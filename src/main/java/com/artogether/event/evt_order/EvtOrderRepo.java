package com.artogether.event.evt_order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EvtOrderRepo extends JpaRepository<EvtOrder, Integer> {
    List<EvtOrder> findByMemberId(Integer memberId);
}
