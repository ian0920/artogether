package com.artogether.event.evt_order;

import com.artogether.event.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvtOrderService {

    @Autowired
    private EvtOrderRepo repo;

    //null handling not be done
    public EvtOrder findById(int id) {
        return repo.findById(id).orElse(null);
    }

    public List<EvtOrder> findAllEvtOrders() {
        return repo.findAll();
    }

    public EvtOrder saveEvtOrder(EvtOrder evtOrder) {
        return repo.save(evtOrder);
    }

    public EvtOrder updateStatus(EvtOrder evtOrder) {
        EvtOrder eo = findById(evtOrder.getId());
        eo.setStatus(evtOrder.getStatus());
        return repo.save(eo);

    }

    public Map<Event, EvtOrder> getEventsToMyOrders(Integer memberId) {
        Map<Event, EvtOrder> eventsToMyOrders = new HashMap();
        List<EvtOrder> myOrders = repo.findByMemberId(memberId);
        myOrders.forEach((o) -> {
            eventsToMyOrders.put(o.getEvent(), o);
        });
        return eventsToMyOrders;
    }


}
