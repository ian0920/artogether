package com.artogether.controller.ian;

import com.artogether.common.member.MemberService;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_order.EvtOrder;
import com.artogether.event.evt_order.EvtOrderService;
import com.artogether.event.my_evt_coup.MyEvtCoup;
import com.artogether.event.my_evt_coup.MyEvtCoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Predicate;

@Controller
@RequestMapping("evt")
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    EvtOrderService evtOrderService;
    @Autowired
    MemberService memberService;

    @Autowired
    MyEvtCoupService myEvtCoupService;

    public EventController() {
    }

    @GetMapping({"all"})
    public String allEvents(Model model) {
        model.addAttribute("events", null);
        return "event/events";
    }

    @PostMapping({"all"})
    public String allEvents(String search, Model model) {

        List<Event> eventList = eventService.findAllEvents(search);
        model.addAttribute("events", eventList);
        return "event/events";
    }

    @GetMapping({"orders"})
    public String order(Model model, HttpSession session) {

        Integer memberId = (Integer)session.getAttribute("member");
        Map<Event, EvtOrder> map = evtOrderService.getEventsToMyOrders(memberId);
        model.addAttribute("orders", map);
        return "event/orders";
    }


    //瀏覽活動
    @GetMapping("event")
    public String event(Model model, HttpServletRequest request, HttpSession session) {


        /*
        * 優惠券日期期限尚未確認
        * */


        Integer memberId = (Integer)session.getAttribute("member");
        Integer eventId = Integer.parseInt(request.getParameter("eventId"));
        Event e = eventService.findById(eventId);


        //確認是否已報名過此活動 (已報名則報名按鈕disable)
        Map<Event, EvtOrder> map = evtOrderService.getEventsToMyOrders(memberId);
        Predicate<Event> filter = p -> Objects.equals(p.getId(), eventId);
        boolean match = map.keySet().stream().anyMatch(filter);

        if(match){
            model.addAttribute("match", true);
        } else {
            model.addAttribute("match", false);
        }

        //找出此會員所擁有此活動的優惠券
        List<MyEvtCoup> myEvtCoups = myEvtCoupService.getMyEvtCoupsByMemberIdAndEventId(memberId, eventId);

        //確認取出的優惠券是未使用的 status = 0
        List<MyEvtCoup> filteredMyEvtCoups = myEvtCoups.stream().filter(mec -> mec.getStatus() == 0).toList();
        model.addAttribute("eventObject", e);

        Map<Integer, Double> evtCoupIdAndTypeMap = new HashMap<>();
        filteredMyEvtCoups.forEach(m -> evtCoupIdAndTypeMap.put(m.getEvtCoup().getId(), m.getEvtCoup().getRatio()== null ? m.getEvtCoup().getDeduction() : m.getEvtCoup().getRatio()));

        model.addAttribute("myEvtCoups", filteredMyEvtCoups);
        model.addAttribute("evtCoupIdAndTypeMap", evtCoupIdAndTypeMap);

        return "event/eventTest2";

    }

    //活動報名
    @PostMapping("enroll")
    public String eventEnroll(EvtOrder evtOrder,Integer eventId, HttpSession session, Model model) {

        /*
         *
         * 確認是否已報名(避免重複報名)v  -> 活動報名按鈕disabled
         * 報名訂單成立v
         * 活動報名人數調整
         * 優惠券狀態變更
         *
         */

        List<String> errors = new ArrayList<>();
        Integer memberId = (Integer) session.getAttribute("member");


        Map<Event, EvtOrder> map = evtOrderService.getEventsToMyOrders(memberId);
        Predicate<Event> filter = e -> Objects.equals(e.getId(), eventId);
        boolean match = map.keySet().stream().anyMatch(filter);
        if (match) {
            model.addAttribute("errors", errors);
            return "event/eventTest2";
        }



        evtOrderService.saveEvtOrder(evtOrder,memberId, eventId);

        return "Test_success";
    }
}
