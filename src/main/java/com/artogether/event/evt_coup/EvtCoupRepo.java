package com.artogether.event.evt_coup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvtCoupRepo extends JpaRepository<EvtCoup, Integer> {
    List<EvtCoup> findByEvent_BusinessMember_IdOrderByEventId(Integer businessMemberId);


    List<EvtCoup> findEvtCoupsByEvent_BusinessMember_Id(Integer businessId);

    List<EvtCoup> findAllByStatus(Byte status);
}
