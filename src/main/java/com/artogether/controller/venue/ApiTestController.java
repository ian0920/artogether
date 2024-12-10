package com.artogether.controller.venue;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.util.BinaryTools;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.TslotDTO;
import com.artogether.venue.vnedto.VneCardDTO;
import com.artogether.venue.vnedto.VneDetailDTO;
import com.artogether.venue.vnedto.VnePriceDTO;
import com.artogether.venue.vneimg.VneImgService;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/vne")
public class ApiTestController {

    @Autowired
    private VneImgService vneImgService;
    @Autowired
    private VenueService venueService;
    @Autowired
    private TslotService tslotService;
    @Autowired
    private VnePriceService vnePriceService;

    //取出該商家所有場地
    @GetMapping("/vneList")
    public List<VneCardDTO> vneListApi(HttpSession session) {
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        int businessId = businessMember.getId();
        List<VneCardDTO> vneCardDTOs = venueService.bizVneList(businessId);
        return vneCardDTOs;
    }

    //取出該場地的細節
    @GetMapping("/Detail/{vneId}")
    public VneDetailDTO vneDetail(@PathVariable("vneId") Integer vneId) {
        VneDetailDTO vneDetailDTO = venueService.getDetailVenue(vneId);
        return vneDetailDTO;
    }

    //取出該場地的時間細節
    @GetMapping("/tslot/{vneId}")
    public TslotDTO nearestTslot(@PathVariable("vneId") Integer vneId) {
        LocalDateTime now = LocalDateTime.now();
        TslotDTO tslotDTO = tslotService.nearestTslot(vneId,now);
        return tslotDTO;
    }
    @GetMapping("/price/{vneId}")
    public VnePriceDTO nearestPrice(@PathVariable("vneId") Integer vneId) {
        LocalDateTime now = LocalDateTime.now();
        VnePriceDTO vnePriceDTO =vnePriceService.getNearestVnePrice(vneId, now);
        System.out.println(vnePriceDTO);
        return vnePriceDTO;
    }
    @PostMapping("/test1")
    public List<VneCardDTO> testApi1() {
        int businessId = 1;
        List<VneCardDTO> vneCardDTOs = venueService.bizVneList(businessId);
        return vneCardDTOs;
    }
}
