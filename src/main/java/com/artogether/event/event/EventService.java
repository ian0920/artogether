package com.artogether.event.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;


    //null handling not be done
    public Event findById(int id) {
        return eventRepo.findById(id).orElse(new Event());
    }

    public List<Event> findAllEvents() {
        return eventRepo.findAll();
    }

    public Event saveEvent(Event event) {
        return eventRepo.save(event);
    }

    public void statusUpdate(Event event) {
        Event e = findById(event.getId());
        e.setStatus(event.getStatus());
        eventRepo.save(e);
    }

}
