package com.artogether.event.evt_order;

import com.artogether.common.member.MemberService;
import com.artogether.event.dto.EvtOrderDTO;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_coup.EvtCoup;
import com.artogether.event.evt_coup.EvtCoupRepo;
import com.artogether.event.evt_coup.EvtCoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
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

    @Autowired
    private EvtOrderDAO evtOrderDAO;
    @Autowired
    private EvtCoupService evtCoupService;
    @Autowired
    private EvtCoupRepo evtCoupRepo;

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

    public Map<Event, EvtOrder> getEventsToMyOrders(Integer memberId, boolean filter) {
        Map<Event, EvtOrder> eventsToMyOrders = new HashMap();
        List<EvtOrder> myOrders = repo.findByMemberId(memberId);


        //是否要過濾掉報名狀態
        if(filter){
            //過濾掉已經取消報名的訂單，回傳只有報名中的訂單
            myOrders.stream().filter(e -> e.getStatus() != 1 ).forEach((o) -> {
                eventsToMyOrders.put(o.getEvent(), o);
            });
        } else {
            myOrders.forEach(o -> eventsToMyOrders.put(o.getEvent(), o));
        }

        return eventsToMyOrders;
    }

    public Page<EvtOrder> getEventToMyOrdersPagination(Integer memberId,int page, int size, String sortBy, String status, String eventStatus ) {
        Map<Event, EvtOrder> eventsToMyOrders = new HashMap();
        List<EvtOrder> myOrders = repo.findByMemberId(memberId);
        myOrders.forEach((o) -> {
            eventsToMyOrders.put(o.getEvent(), o);
        });

        Comparator<EvtOrder> finalComparator = null;


        //照訂單日期排序
        Comparator<EvtOrder> orderDateComparator = new Comparator<EvtOrder>() {
            public int compare(EvtOrder o1, EvtOrder o2) {
                return o1.getOrderDate().compareTo(o2.getOrderDate());
            }
        };

        //照活動日期排序
        Comparator<EvtOrder> eventDateComparator = new Comparator<EvtOrder>() {
            public int compare(EvtOrder o1, EvtOrder o2) {
                return o1.getEvent().getStartDate().compareTo(o2.getEvent().getStartDate());
            }
        };

        //照訂單id排序(預設)
        Comparator<EvtOrder> idComparator = new Comparator<EvtOrder>() {
            public int compare(EvtOrder o1, EvtOrder o2) {
                return o1.getId().compareTo(o2.getId());
            }
        };

        switch (sortBy) {
            case "orderDate":
            case "orderDateR":
                finalComparator = orderDateComparator;
                break;
            case "eventDate":
            case "eventDateR":
                finalComparator = eventDateComparator;
                break;
            default:
                finalComparator = idComparator;
        }


        if(sortBy.contains("R")){
            finalComparator = finalComparator.reversed();
        }

        //依活動報名狀態篩選(0→已付款 1→取消)
        if(!"".equals(status)) {
            myOrders = myOrders.stream().filter(m -> m.getStatus()==Byte.parseByte(status)).toList();
        }

        //依照活動狀態篩選(1→上架 2→延期 3→取消 5→結束)
        if(!"".equals(eventStatus)) {
            myOrders = myOrders.stream().filter(m -> m.getEvent().getStatus()==Byte.parseByte(eventStatus)).toList();
        }

        myOrders.sort(finalComparator);

        int start = page * size;
        int end = Math.min(start + size, myOrders.size());

        List<EvtOrder> paginatedEventList = myOrders.subList(start, end);
        Pageable pageable = PageRequest.of(page,size);


        return new PageImpl<>(paginatedEventList, pageable, myOrders.size());
    }

    public void cancelOrder(Integer orderId) {

        repo.findById(orderId).ifPresent((o) -> {
            if(o.getStatus() == 0) {
                o.setStatus((byte)1);
                repo.save(o);
            }

        });
    }


    public Page<EvtOrderDTO> findEvtOrders(Integer businessId, Integer eventId, Timestamp formattedStartDate,
                                        Timestamp formattedEndDate, Integer status, int page, int size) {

        List<EvtOrder> result = evtOrderDAO.getEvtOrderByCriteria(businessId, eventId, formattedStartDate,formattedEndDate, status, page, size);

        //從會員id找出所有活動的優惠券，並將優惠券的id跟名字放入map
        Map<Integer, String> evtCoupIdNameMap = new HashMap();
        List<EvtCoup> evtCoupList = evtCoupRepo.findEvtCoupsByEvent_BusinessMember_Id(businessId);
        evtCoupList.forEach(e -> evtCoupIdNameMap.put(e.getId(),e.getEvtCoupName()));

        //將EvtOrder轉成DTO傳出去 1.透過DTOTransformer轉成DTO，再透過Map去對應EvtCoupId跟EvtCoupName並將EvtCoupName設進DTO
        List<EvtOrderDTO> DTOresult = result.stream().map(EvtOrderDTO::EvtOrderDTOTransformer)
                .map(e ->{
                    if(evtCoupIdNameMap.containsKey(e.getEvtCoupId()))
                        e.setEvtCoupName(evtCoupIdNameMap.get(e.getEvtCoupId()));
                    return e;
                }).toList();

        long totalCount = evtOrderDAO.countEvtOrderByCriteria(businessId, eventId, formattedStartDate, formattedEndDate, status);


        Pageable pageable = PageRequest.of(page,size);

        return new PageImpl<>(DTOresult, pageable, totalCount);
    }
}
