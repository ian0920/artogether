package com.artogether.controller.ian;

import com.artogether.event.evt_coup.EvtCoup;
import com.artogether.event.evt_coup.EvtCoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("business_event")
public class RestEventBusinessController {

    @Autowired
    EvtCoupService evtCoupService;

    //優惠券管理
    @GetMapping("/evtCoup")
    public List<EvtCoup> couponManagement() {
        return evtCoupService.findAllEvtCoup();
    }
}
