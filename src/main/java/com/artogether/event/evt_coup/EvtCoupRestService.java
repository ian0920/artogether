package com.artogether.event.evt_coup;

import com.artogether.event.dto.EvtCoupDTO;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class EvtCoupRestService {

    @Autowired
    EvtCoupRepo evtCoupRepo;

    @Autowired
    EventRepo eventRepo;

    @Autowired
    EvtCoupDAO evtCoupDAO;

    public Map<Integer, String> getEventNameAndIdByBusinessId (Integer businessId) {
        Map<Integer, String> eventNameAndId = new HashMap<>();
        List<Event> events = eventRepo.findByBusinessMember_Id(businessId);
        events.forEach(event -> eventNameAndId.put(event.getId(), event.getName()));
        return eventNameAndId;
    }

    public List<EvtCoupDTO> findAllEvtCoupByBusinessId(Integer businessId) {

        List<EvtCoup> evtCoups = evtCoupRepo.findByEvent_BusinessMember_IdOrderByEventId(businessId);
        return evtCoups.stream().map(EvtCoupDTO::transformFromEvtCoup).toList();

    }

    public List<EvtCoupDTO> findByCompositeQuery(Integer eventId, String couponName, byte type, Timestamp startDate, Timestamp endDate, byte status) {


        if (eventId != -1) {
            List<EvtCoup> result = evtCoupDAO.getEvtCoupsByCriteria(eventId, couponName, type, startDate, endDate, status);
            return result.stream().map(EvtCoupDTO::transformFromEvtCoup).toList();
        }

        return null;
    }
}
