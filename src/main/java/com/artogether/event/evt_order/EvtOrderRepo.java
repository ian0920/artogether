package com.artogether.event.evt_order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;


@Repository
public interface EvtOrderRepo extends JpaRepository<EvtOrder, Integer> {
    List<EvtOrder> findByMemberId(Integer memberId);

    List<EvtOrder> findAllByEvent_idInAndAndOrderDateBetween(List<Integer> businessId, Timestamp startDate, Timestamp endDate);
}
