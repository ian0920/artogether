package com.artogether.event.evt_coup;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberRepo;
import com.artogether.event.dto.EvtCoupDTO;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventRepo;
import com.artogether.event.my_evt_coup.MyEvtCoup;
import com.artogether.event.my_evt_coup.MyEvtCoupRepo;
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

    @Autowired
    MemberRepo memberRepo;

    @Autowired
    MyEvtCoupRepo myEvtCoupRepo;

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

    public EvtCoup addNewEvtCoup(EvtCoup evtCoup, Integer eventId) {

        /*
            將優惠券新增進去資料庫v
            將優惠券發放給所有會員: 撈出所有會員(已啟用 1) -> 要分發的優惠券id -> 寫進我的優惠券資料庫(新執行緒)
         */


        //存入資料庫
        Event e = eventRepo.getReferenceById(eventId);
        evtCoup.setEvent(e);

        EvtCoup newEvtCoup = evtCoupRepo.save(evtCoup);

        //若優惠券狀態是上架則分發給所有會員(另建執行緒來執行)
        if(newEvtCoup.getStatus() == 1){

            new Thread(() -> deliverCoupon(newEvtCoup)).start();

        }

        return newEvtCoup;

    }


    //factory method for 發送優惠券給所有會員
    public void deliverCoupon(EvtCoup newEvtCoup){

        List<Member> memberList = memberRepo.findAll();
        memberList.forEach(m -> myEvtCoupRepo.save(new MyEvtCoup(m, newEvtCoup, (byte) 0, new Timestamp(System.currentTimeMillis()),null)));

    }
}
