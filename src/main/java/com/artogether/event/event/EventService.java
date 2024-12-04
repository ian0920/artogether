package com.artogether.event.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;


    //null handling not be done
    public Event findById(int id) {
        return eventRepo.findById(id).orElse(null);
    }

    public List<Event> findAllEvents(String search) {

        Comparator<Event> finalComparator = null;

        //照報名人數排序
        Comparator<Event> enrollComparator = new Comparator<Event>() {
            public int compare(Event o1, Event o2) {
                return o1.getEnrolled() - o2.getEnrolled();
            }
        };

        //照價格排序
        Comparator<Event> priceComparator = new Comparator<Event>() {
            public int compare(Event o1, Event o2) {
                return o1.getPrice() - o2.getPrice();
            }
        };

        //照活動日期排序
        Comparator<Event> startDateComparator = new Comparator<Event>() {
            public int compare(Event o1, Event o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        };

        //照活動id = 新舊排序(默認)
        Comparator<Event> defaultComparator = new Comparator<Event>() {
            public int compare(Event o1, Event o2) {
                return o2.getId() - o1.getId();
            }
        };

        switch (search) {
            case "price":
            case "priceR":
                finalComparator = priceComparator;
                break;
            case "startDate":
            case "startDateR":
                finalComparator = startDateComparator;
                break;
            case "enrolled":
            case "enrolledR":
                finalComparator = enrollComparator;
                break;
            default:
                finalComparator = defaultComparator;
        }

        if (search.contains("R")) {
            finalComparator = finalComparator.reversed();
        }

        return this.eventRepo.findAll().stream().sorted(finalComparator).toList();
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
