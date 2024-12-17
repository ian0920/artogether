package com.artogether.event.evt_order;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberRepo;
import com.artogether.common.member.MemberService;
import com.artogether.event.dto.EvtOrderDTO;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventRepo;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_coup.EvtCoup;
import com.artogether.event.evt_coup.EvtCoupRepo;
import com.artogether.event.evt_coup.EvtCoupService;
import com.artogether.event.my_evt_coup.MyEvtCoup;
import com.artogether.event.my_evt_coup.MyEvtCoupRepo;
import com.artogether.util.ApiResponse;
import com.artogether.util.EnrollmentCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

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
    @Autowired
    private EvtOrderRepo evtOrderRepo;
    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private MyEvtCoupRepo myEvtCoupRepo;

    private final ApplicationEventPublisher eventPublisher;

    public EvtOrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    //null handling not be done
    public EvtOrder findById(int id) {
        return repo.findById(id).orElse(null);
    }

    public List<EvtOrder> findAllEvtOrders() {
        return repo.findAll();
    }


    //(Deprecate)
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


    //活動報名取消
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<EvtOrder> cancelOrder(Integer orderId) {


        EvtOrder o = repo.findById(orderId).orElseThrow(() -> new RuntimeException("活動訂單不存在"));

        Event event = eventRepo.findById(o.getEvent().getId()).orElseThrow(() -> new RuntimeException("活動不存在"));
        Timestamp today = new Timestamp(System.currentTimeMillis());

        try{

            // 確認訂單狀態為未取消
            if(o.getStatus() == 0 ) {

                //活動正常舉行 且取消時間在活動開始三天前
                if (event.getStatus() == 1 && (today.getTime() + 86400000*3) <= event.getStartDate().getTime()){


                    //調整訂單狀態、調整活動報名人數
                    o.setStatus((byte) 1);
                    repo.save(o);

                    event.setEnrolled(event.getEnrolled() -1);
                    eventRepo.save(event);

                    return new ApiResponse<EvtOrder>(true, "活動報名已取消", null, null);
                }


                //延期活動取消報名  且取消時間在活動開始三天前
                if(event.getStatus() == 2 && (today.getTime() + 86400000*3) <= event.getDelayDate().getTime()){


                    //調整訂單狀態、調整活動報名人數
                    o.setStatus((byte) 1);
                    repo.save(o);

                    event.setEnrolled(event.getEnrolled() -1);
                    eventRepo.save(event);

                    return new ApiResponse<EvtOrder>(true, "活動報名已取消", null, null);
                }

                return new ApiResponse<EvtOrder>(false, "取消報名需在活動開始三天前", null, null);

            } else {

                return new ApiResponse<EvtOrder>(false, "報名已取消，請勿重複申請", null, null);
            }

        }catch (Exception e){

            e.printStackTrace();
            return new ApiResponse<EvtOrder>(false, "取消報名失敗，請稍後再試", null, null);
        }


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

    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<EvtOrderDTO> eventEnroll(EvtOrderDTO evtOrderDTO) {


        /*
         * 確認活動狀態 1->報名中 2->延期 V
         * 確認活動報名人數是否額滿(service) V
         * 報名訂單成立(service) V
         * 活動報名人數調整(service) V
         * 優惠券狀態變更(service) V
         */


        Event event = eventRepo.findById(evtOrderDTO.getEventId())
                .orElseThrow(()-> new RuntimeException("沒有此活動"));

        Member member = memberRepo.findById(evtOrderDTO.getMemberId())
                .orElseThrow(()-> new RuntimeException("找不到此會員資料"));

        /* 確認活動狀態(可報名) */

        if (event.getStatus() != 1 && event.getStatus() != 2) {

            return new ApiResponse<EvtOrderDTO>(false, "活動無法報名", null, null);
        }


        /* 確認活動額滿狀態 */

        //報名人數尚未超過活動上限人數
        if ((event.getEnrolled() + evtOrderDTO.getQty()) < event.getMaximum()){


            //報名訂單成立、活動報名人數調整、優惠券狀態變更
            try{

                /* 報名訂單成立 */
                EvtOrder newOrder = new EvtOrder();

                newOrder.setEvent(event);
                newOrder.setMember(member);
                newOrder.setQty(evtOrderDTO.getQty());
                if ( evtOrderDTO.getEvtCoupId()!=null )
                    newOrder.setEvtCoupId(evtOrderDTO.getEvtCoupId());
                newOrder.setPaymentMethod(evtOrderDTO.getPaymentMethod());
                newOrder.setTotalPrice(evtOrderDTO.getTotalPrice());
                newOrder.setDiscount(evtOrderDTO.getDiscount());
                newOrder.setPaid(evtOrderDTO.getPaid());
                newOrder.setStatus((byte) 0);

                EvtOrder orderSaved = evtOrderRepo.save(newOrder);

                /* 活動報名人數調整 */

                event.setEnrolled(event.getEnrolled() + evtOrderDTO.getQty());
                eventRepo.save(event);


                /* 優惠券狀態變更 */

                if (evtOrderDTO.getEvtCoupId() != null){
                    Optional<MyEvtCoup> myEvtCoup =
                            myEvtCoupRepo.findById(new MyEvtCoup.Composite(evtOrderDTO.getMemberId(), evtOrderDTO.getEvtCoupId()));

                    //若優惠券狀態為未使用，更改狀態為已使用
                    myEvtCoup.filter(e -> e.getStatus() == 0).ifPresent(
                            e -> {
                                e.setStatus((byte) 1);
                                myEvtCoupRepo.save(e);
                            });

                }

                eventPublisher.publishEvent(new EnrollmentCompletedEvent(this, event.getId()));

                EvtOrderDTO orderDTO = EvtOrderDTO.EvtOrderDTOTransformer(orderSaved);

                return new ApiResponse<EvtOrderDTO>(true, "報名成功", orderDTO, null);


            }catch (Exception e){
                e.printStackTrace();
                return new ApiResponse<>(false, "報名時發生問題請稍後再試", null, null);
            }

        } else {
            //報名人數超過超過活動人數上限

            return new ApiResponse<EvtOrderDTO>(false, "報名人數超過活動上限", null, null);
        }


    }

    public List<EvtOrderDTO> findOrdersForAccounting(Integer businessId, Timestamp startDate, Timestamp endDate) {

        List<Event> eventList = eventRepo.findByBusinessMember_Id(businessId);
        List<Integer> eventIdToBusinessMember = new ArrayList<>();
        eventList.forEach(e -> eventIdToBusinessMember.add(e.getId()));

        List<EvtOrder> orders = evtOrderRepo.findAllByEvent_idInAndAndOrderDateBetween(eventIdToBusinessMember,startDate,endDate);
        List<EvtOrderDTO> orderDTOList = orders.stream().map(EvtOrderDTO::EvtOrderDTOTransformer).toList();

        System.out.println(orderDTOList.size());

        return orderDTOList;

    }
}
