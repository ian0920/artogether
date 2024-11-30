package com.artogether.controller.ian;

import com.artogether.common.member.MemberService;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_order.EvtOrder;
import com.artogether.event.evt_order.EvtOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    EvtOrderService evtOrderService;
    @Autowired
    MemberService memberService;

    public EventController() {
    }

    @GetMapping({"evt/all"})
    public String allEvents(Model model) {
        model.addAttribute("events", null);
        return "event/events";
    }

    @PostMapping({"evt/all"})
    public String allEvents(String search, Model model) {

        List<Event> eventList = this.eventService.findAllEvents(search);
        model.addAttribute("events", eventList);
        return "event/events";
    }

    @GetMapping({"evt/orders"})
    public String order(Model model, HttpSession session) {

        Integer memberId = (Integer)session.getAttribute("member");
        Map<Event, EvtOrder> map = evtOrderService.getEventsToMyOrders(memberId);
        model.addAttribute("orders", map);
        return "event/orders";
    }

    @GetMapping({"evt/enroll"})
    public String enroll(EvtOrder evtOrder, Integer evtId, HttpSession session) {

        Integer memberId = (Integer)session.getAttribute("member");
        evtOrder.setEvent(this.eventService.findById(evtId));
        evtOrder.setMember(this.memberService.findById(memberId));

        evtOrderService.saveEvtOrder(evtOrder);
        Event e = eventService.findById(evtId);
        e.setEnrolled(e.getEnrolled() + 1);
        eventService.saveEvent(e);

        return "redirect:/";
    }
}
