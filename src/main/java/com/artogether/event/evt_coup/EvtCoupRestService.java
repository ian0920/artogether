package com.artogether.event.evt_coup;

import com.artogether.event.dto.EvtCoupDTO;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public EvtCoup updateEvtCoup(EvtCoup evtCoup) {

        Optional<EvtCoup> origin = evtCoupRepo.findById(evtCoup.getId());


        if (origin.isPresent()) {
            EvtCoup update = origin.get();
            update.setEvtCoupName(evtCoup.getEvtCoupName());
            update.setStartDate(evtCoup.getStartDate());
            update.setEndDate(evtCoup.getEndDate());
            update.setType(evtCoup.getType());
            update.setRatio(evtCoup.getRatio());
            update.setDeduction(evtCoup.getDeduction());
            update.setDuration(evtCoup.getDuration());
            update.setThreshold(evtCoup.getThreshold());
            update.setStatus(evtCoup.getStatus());
            return evtCoupRepo.save(update);
        }

        return null;
    }
}
