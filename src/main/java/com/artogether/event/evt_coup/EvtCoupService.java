package com.artogether.event.evt_coup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvtCoupService {

    @Autowired
    private EvtCoupRepo evtCoupRepo;

    //null handling not be done
    public EvtCoup findById(int id) {
        return evtCoupRepo.findById(id).orElse(new EvtCoup());
    }

    public List<EvtCoup> findAllEvtCoup() {
        return evtCoupRepo.findAll();
    }

    public EvtCoup saveEvtCoup(EvtCoup evtCoup) {
        return evtCoupRepo.save(evtCoup);
    }

    public void statusUpdate(EvtCoup evtCoup) {
        EvtCoup ec = findById(evtCoup.getId());
        ec.setStatus(evtCoup.getStatus());
        evtCoupRepo.save(ec);
    }

}
