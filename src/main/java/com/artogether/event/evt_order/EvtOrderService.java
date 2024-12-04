package com.artogether.event.evt_order;

import com.artogether.common.member.MemberService;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvtOrderService {

    @Autowired
    private EvtOrderRepo repo;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EventService eventService;

    //null handling not be done
    public EvtOrder findById(int id) {
        return repo.findById(id).orElse(null);
    }

    public List<EvtOrder> findAllEvtOrders() {
        return repo.findAll();
    }

    public EvtOrder saveEvtOrder(EvtOrder evtOrder, Integer memberId, Integer eventId) {

        EvtOrder evt = EvtOrder.builder()
                .member(memberService.findById(memberId))
                .event(eventService.findById(eventId))
                .qty(evtOrder.getQty())
                .paymentMethod(evtOrder.getPaymentMethod())
                .evtCoupId(evtOrder.getEvtCoupId())
                .totalPrice(evtOrder.getTotalPrice())
                .paid(evtOrder.getPaid())
                .discount(evtOrder.getDiscount())
                .status((byte)0)
                .build();

        return repo.save(evt);
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
