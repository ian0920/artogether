package com.artogether.controller.ian;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.event.dto.EvtCoupDTO;
import com.artogether.event.dto.EvtOrderDTO;
import com.artogether.event.dto.EvtOrderSearchCriteria;
import com.artogether.event.dto.PaginationMetadata;
import com.artogether.event.evt_coup.EvtCoup;
import com.artogether.event.evt_coup.EvtCoupRestService;
import com.artogether.event.evt_order.EvtOrderService;
import com.artogether.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("business/event")
public class RestEventBusinessController {

    @Autowired
    EvtCoupRestService evtCoupRestService;
    @Autowired
    private EvtOrderService evtOrderService;

    //優惠券頁面呈現
    @GetMapping("coupons")
    public ModelAndView coupons(HttpSession session, ModelAndView mv) {

        //查詢該商家所有活動的名稱Map<活動ID,活動名稱>
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        Map<Integer, String> eventMap = evtCoupRestService.getEventNameAndIdByBusinessId(businessMember.getId());

        mv.addObject("eventMap", eventMap);
        mv.setViewName("event/event_coupon_management");


        return mv;
    }

    //查詢所有優惠券
    @GetMapping("/evtCoups")
    public ResponseEntity<ApiResponse<List<EvtCoupDTO>>> allCoupons (HttpSession session) {

        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        List<EvtCoupDTO> evtCoupDTOs = evtCoupRestService.findAllEvtCoupByBusinessId(businessMember.getId());

        if(evtCoupDTOs.isEmpty())
            return ResponseEntity.ok(new ApiResponse<>(false, "尚未有優惠券", null,null ));

        return ResponseEntity.ok(new ApiResponse<>(true, "操作成功", evtCoupDTOs,null ));
    }

    //複合查詢優惠券
    @GetMapping("searchCoupons")
    public ResponseEntity<ApiResponse<List<EvtCoupDTO>>> couponSearch (
            @RequestParam(value = "eventId") Integer eventId,
            @RequestParam(value = "couponName", required = false) String couponName,
            @RequestParam(value = "discountType", required = false) byte type,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "couponStatus", required = false) byte status) {

        Timestamp formattedStartDate = null;
        Timestamp formattedEndDate = null;

        //前端type="datetime-local" format 為 yyyy-MM-dd'T'HH:mm
        //java.sql.Timestamp format 為 yyyy-MM-dd HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


        try{
            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime s = LocalDateTime.parse(startDate, formatter);
                formattedStartDate = Timestamp.valueOf(s);
            }
            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime e = LocalDateTime.parse(endDate, formatter);
                formattedEndDate = Timestamp.valueOf(e);
            }
        } catch (IllegalArgumentException e){
            return ResponseEntity.ok(new ApiResponse<>(false, "日期格式有誤", null,null ));
        }



        List<EvtCoupDTO> result =
                evtCoupRestService.findByCompositeQuery(eventId, couponName, type, formattedStartDate, formattedEndDate, status);


        if (!result.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "搜尋完成", result, null));
        }

        return ResponseEntity.ok(new ApiResponse<>(false, "沒有相關搜尋結果", null,null));
    }

    //修改優惠券
    @PutMapping("coupons/update")
    public ResponseEntity<ApiResponse<EvtCoupDTO>> updateCoupon (@RequestBody EvtCoup evtCoup) {


        EvtCoup updatedEvtCoup = evtCoupRestService.updateEvtCoup(evtCoup);
        if (updatedEvtCoup != null){
            EvtCoupDTO updatedEvtCoupDTO = EvtCoupDTO.transformFromEvtCoup(updatedEvtCoup);
            return ResponseEntity.ok(new ApiResponse<>(true, "優惠券更新成功",updatedEvtCoupDTO, null));
        }

        return ResponseEntity.ok(new ApiResponse<>(false, "優惠券更新失敗，請再重試一次", null, null));
    }

    //新增優惠券
    @PostMapping("coupons/add")
    public ResponseEntity<ApiResponse<EvtCoupDTO>> addCoupon(@RequestBody EvtCoup evtCoup, @RequestParam Integer eventId){


        EvtCoup newEvtcoup = evtCoupRestService.addNewEvtCoup(evtCoup, eventId);
        EvtCoupDTO newEvtCoupDTO = EvtCoupDTO.transformFromEvtCoup(newEvtcoup);

        return ResponseEntity.ok(new ApiResponse<>(true, "優惠券新增成功", newEvtCoupDTO, null));
    }


    //活動訂單頁面呈現
    @GetMapping("orders")
    public ModelAndView order (ModelAndView mv, HttpSession session) {

        //查詢該商家所有活動的名稱Map<活動ID,活動名稱>
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        Map<Integer, String> eventMap = evtCoupRestService.getEventNameAndIdByBusinessId(businessMember.getId());

        mv.addObject("eventMap", eventMap);
        mv.setViewName("event/event_order_management");
        return mv;
    }

    //搜尋訂單
    @PostMapping("searchOrders")
    public ResponseEntity<ApiResponse<Page<EvtOrderDTO>>> orderSearch (
            @RequestBody EvtOrderSearchCriteria searchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session)
    {

        Integer businessId = ((BusinessMember)session.getAttribute("presentBusinessMember")).getId();

        Timestamp formattedStartDate = null;
        Timestamp formattedEndDate = null;

        //前端type="datetime-local" format 為 yyyy-MM-dd'T'HH:mm
        //java.sql.Timestamp format 為 yyyy-MM-dd HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        try{
            if (searchCriteria.getStartDate() != null && !searchCriteria.getStartDate().isEmpty()) {
                LocalDateTime s = LocalDateTime.parse(searchCriteria.getStartDate(), formatter);
                formattedStartDate = Timestamp.valueOf(s);
            }
            if (searchCriteria.getEndDate() != null && !searchCriteria.getEndDate().isEmpty()) {
                LocalDateTime e = LocalDateTime.parse(searchCriteria.getEndDate(), formatter);
                formattedEndDate = Timestamp.valueOf(e);
            }
        } catch (IllegalArgumentException e){
            return ResponseEntity.ok(new ApiResponse<>(false, "日期格式有誤", null,null ));
        }


        Page<EvtOrderDTO> searchResult =
                evtOrderService.findEvtOrders(businessId, searchCriteria.getEventId(), formattedStartDate, formattedEndDate, searchCriteria.getStatus(), page, size);


        PaginationMetadata metadata = new PaginationMetadata(searchResult);

        return ResponseEntity.ok(new ApiResponse<>(true, "搜尋成功", searchResult, metadata));
    }
}
