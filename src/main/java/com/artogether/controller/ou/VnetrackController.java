package com.artogether.controller.ou;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.event.dto.EvtOrderDTO;
import com.artogether.venue.vne_track.VneSearchService;
import com.artogether.venue.vneorder.VneOrder;
import com.artogether.venue.vneorder.VneOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("platform")
public class VnetrackController {

    @Autowired
    private BusinessService businessService;
    @Autowired
    private VneOrderService vneOrderService;
    @Autowired
    private VneSearchService vneSearchService;

    @GetMapping("vne/accounting")
    public String eventAccounting(Model model){

        List<BusinessMember> businessMember = businessService.findAll();
        model.addAttribute("businessMember", businessMember);

        return "platform/vne_accounting";
    }

    @PostMapping("vne/accounting/search")
    public String eventAccountingSearch(Integer businessId, String startDate, String endDate, Model model) {

        List<VneOrder> order = vneSearchService.findVneAccounting(businessId, startDate, endDate);
        List<BusinessMember> businessMember = businessService.findAll();
//        System.out.println(businessMember);
        model.addAttribute("businessMember", businessMember);
        model.addAttribute("orders", order);

        return "platform/vne_accounting";
    }
}
