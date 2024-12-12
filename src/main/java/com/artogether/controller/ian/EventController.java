package com.artogether.controller.ian;

import com.artogether.common.member.MemberService;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_order.EvtOrder;
import com.artogether.event.evt_order.EvtOrderService;
import com.artogether.event.my_evt_coup.MyEvtCoup;
import com.artogether.event.my_evt_coup.MyEvtCoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Predicate;

@Controller
@RequestMapping("event")
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    EvtOrderService evtOrderService;
    @Autowired
    MemberService memberService;

    @Autowired
    MyEvtCoupService myEvtCoupService;




    //活動清單瀏覽+分頁功能+複合查詢
    @GetMapping("all")
    public String allEvents(Model model,
                            @RequestParam(defaultValue = "enrolledR")String sortBy,
                            @RequestParam(defaultValue = "")String location,
                            @RequestParam(defaultValue = "")String catalog,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "9") int size) {



        Page<Event> paginatedEventList = eventService.findAllEventsAndPagination(sortBy,page, size, location, catalog);
        model.addAttribute("events", paginatedEventList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedEventList.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        return "event/events";
    }


    //查詢活動訂單to會員
    @GetMapping({"orders"})
    public String order(Model model, HttpSession session) {

        Integer memberId = (Integer)session.getAttribute("member");
        Map<Event, EvtOrder> map = evtOrderService.getEventsToMyOrders(memberId, false);
        model.addAttribute("orders", map);
        return "event/member_event_orders";
    }

    //刪除活動訂單
    @GetMapping("/order/cancel/{id}")
    public String cancelOrder (@PathVariable Integer id) {

        System.out.println(id);

        evtOrderService.cancelOrder(id);

        return "redirect:/event/orders";
    }

    //瀏覽會員活動優惠券
    @GetMapping("/coupons")
    public String coupons(Model model, HttpSession session) {

        Integer memberId = (Integer)session.getAttribute("member");
        Map<String, List<MyEvtCoup>> map =  myEvtCoupService.findByMemberId(memberId);


        model.addAttribute("coupons", map);

        return "event/member_event_coupons";
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


        //撈出報名中的訂單 (報名中則報名按鈕disable)
        Map<Event, EvtOrder> map = evtOrderService.getEventsToMyOrders(memberId, true);
        Predicate<Event> filter = p -> (Objects.equals(p.getId(), eventId));
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

        return "event/eventTest";

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



        Map<Event, EvtOrder> map = evtOrderService.getEventsToMyOrders(memberId, true);
        Predicate<Event> filter = e -> Objects.equals(e.getId(), eventId);
        boolean match = map.keySet().stream().anyMatch(filter);
        if (match) {
            model.addAttribute("errors", errors);
            return "eventTest";
        }



        evtOrderService.saveEvtOrder(evtOrder,memberId, eventId);

        return "Test_success";
    }



}
