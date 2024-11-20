package com.artogether.event.evt_order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvtOrderService {

    @Autowired
    private EvtOrderRepo repo;

    //null handling not be done
    public EvtOrder findById(int id) {
        return repo.findById(id).orElse(new EvtOrder());
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


}
