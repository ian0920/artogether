package com.artogether.controller.venue;

import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.VneOrderDTO;
import com.artogether.venue.vneorder.VneOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/vneMem")
public class VneMemController {

    @Autowired
    private VenueService venueService;
    @Autowired
    private VneOrderService vneOrderService;

    @GetMapping("/details/{vneId}")
    public String detail(Model model, @PathVariable("vneId") Integer vneId){
        venueService.setName(model, vneId);
        return "venue/member/detail";
    }

    @GetMapping("/dateSearch/{vneId}")
    public String dateSearch(Model model, @PathVariable("vneId") Integer vneId) {
        venueService.setName(model, vneId);
        return "/venue/member/dateSearch";
    }
    @GetMapping("/timeSlotSearch/{vneId}")
    public String timeSlotSearch(Model model, @PathVariable("vneId") Integer vneId) {
        venueService.setName(model, vneId);
        return "/venue/member/timeSlotSearch";
    }

    @GetMapping("/order/mem/list")
    public String orderMemList(Model model, HttpSession session) {
        Integer memId = (Integer) session.getAttribute("member");
        List<VneOrderDTO> memOrderList = vneOrderService.getMemOrderList(memId);
        model.addAttribute("orders", memOrderList);
        return "/venue/member/orderList";
    }
}
