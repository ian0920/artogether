package com.artogether.controller.venue;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.*;
import com.artogether.venue.vneimg.VneImgService;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vne")
public class VneRestController {

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
    @PostMapping("/details")
    public VneDetailDTO vneDetail(@RequestBody Map<String, Integer> request) {
        Integer vneId = request.get("vneId"); // 從請求主體中提取 vneId
        VneDetailDTO vneDetailDTO = venueService.getDetailVenue(vneId); // 根據 vneId 獲取場地詳情
        return vneDetailDTO; // 返回場地詳情 DTO
    }

    @GetMapping("/card/{vneId}")
    public VneCardDTO vneDetail(@PathVariable("vneId") Integer vneId) {
        VneCardDTO vneCardDTO = venueService.getVenue(vneId);
        return vneCardDTO;
    }

    //取出該場地的時間細節
    @GetMapping("/tslot/{vneId}")
    public TslotDTO nearestTslot(@PathVariable("vneId") Integer vneId) {
        LocalDateTime now = LocalDateTime.now();
        TslotDTO tslotDTO = tslotService.nearestTslot(vneId,now);
        return tslotDTO;
    }
    //取出該場地的價錢細節
    @GetMapping("/price/{vneId}")
    public VnePriceDTO nearestPrice(@PathVariable("vneId") Integer vneId) {
        LocalDateTime now = LocalDateTime.now();
        VnePriceDTO vnePriceDTO =vnePriceService.getNearestVnePrice(vneId, now);
        System.out.println(vnePriceDTO);
        return vnePriceDTO;
    }

    //取出該場地的可預約的日期
    @GetMapping("/order/dates/{vneId}")
    public FlatpickrDTO getFlatpickrDTO(@PathVariable("vneId") Integer vneId){
        System.out.println(vneId);
        LocalDateTime now = LocalDateTime.now();
        FlatpickrDTO flatpickrDTO = tslotService.getFlatpickrDTO(vneId, now);
        System.out.println(flatpickrDTO);
        return flatpickrDTO;
    }
    //取出該場地的可預約的時間細節
    @GetMapping("/order/available/{vneId}")
    public AvailableDTO getAvailableDTO(@PathVariable("vneId") Integer vneId,
                                        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return tslotService.getAvailableDTO(vneId, date);
    }
    @PostMapping("/test1")
    public List<VneCardDTO> testApi1() {
        int businessId = 1;
        List<VneCardDTO> vneCardDTOs = venueService.bizVneList(businessId);
        return vneCardDTOs;
    }
}
