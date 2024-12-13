package com.artogether.controller.ian;

import com.artogether.event.evt_order.EvtOrder;
import com.artogether.event.evt_order.EvtOrderService;
import com.artogether.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("event")
public class RestEventController {

    @Autowired
    EvtOrderService evtOrderService;

    @GetMapping("cancel")
    public ResponseEntity<ApiResponse<EvtOrder>> cancelOrder(@RequestParam Integer orderId){

        boolean success = evtOrderService.cancelOrder(orderId);

        if(success){
            return ResponseEntity.ok(new ApiResponse<EvtOrder>(true, "活動報名已取消", null, null));
        }

        return ResponseEntity.ok(new ApiResponse<EvtOrder>(false, "取消報名需在活動開始三天前", null, null));

    }
}
