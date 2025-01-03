package com.artogether.controller.ian;

import com.artogether.event.dto.EvtOrderDTO;
import com.artogether.event.event.Event;
import com.artogether.event.evt_coup.EvtCoup;
import com.artogether.event.evt_coup.EvtCoupService;
import com.artogether.event.evt_order.EvtOrder;
import com.artogether.event.evt_order.EvtOrderService;
import com.artogether.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@RestController
@RequestMapping("event")
public class RestEventController {

    @Autowired
    EvtOrderService evtOrderService;
    @Autowired
    private EvtCoupService evtCoupService;


    //會員取消活動報名
    @GetMapping("cancel")
    public ResponseEntity<ApiResponse<EvtOrder>> cancelOrder(@RequestParam Integer orderId){

        ApiResponse<EvtOrder> response = evtOrderService.cancelOrder(orderId);

        return ResponseEntity.ok(response);

    }

    //會員報名
    @PostMapping("enroll-up")
    public ResponseEntity<ApiResponse<EvtOrderDTO>> eventEnroll(@RequestBody EvtOrderDTO evtOrderDTO, HttpSession session){


        /* 確認是否已有登入會員(前端已擋，後端再擋一次) */

        if (session.getAttribute("member") == null)
            return ResponseEntity.ok(new ApiResponse<EvtOrderDTO>(false, "請先登入會員", null, null));


        /* 確認是否重複報名(前端已擋，後端在再一次) */
        //撈出該會員目前報名中的訂單 (避免重複報名)
        Map<Event, EvtOrder> map = evtOrderService.getEventsToMyOrders(evtOrderDTO.getMemberId(), true);
        Predicate<Event> filter = p -> (Objects.equals(p.getId(), evtOrderDTO.getEventId()));
        boolean match = map.keySet().stream().anyMatch(filter);

        if(match)
            return ResponseEntity.ok(new ApiResponse<EvtOrderDTO>(false, "已報名過此活動", null, null));


        /*   確認優惠券totalPrice > threshold   */

        if (evtOrderDTO.getEvtCoupId() != null){

            EvtCoup evtCoup = evtCoupService.findById(evtOrderDTO.getEvtCoupId());

            if( evtCoup.getThreshold() > evtOrderDTO.getTotalPrice()){

                return ResponseEntity.ok(new ApiResponse<EvtOrderDTO>(false, "未達優惠券使用門檻，門檻：" + evtCoup.getThreshold() + "元", null, null));
            }

        }

        //報名邏輯寫在service層中
        ApiResponse<EvtOrderDTO> response = evtOrderService.eventEnroll(evtOrderDTO);


        return ResponseEntity.ok(response);

    }

}
