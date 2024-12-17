package com.artogether.controller.venue;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.venue.VenueExceptions;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.*;
import com.artogether.venue.vneimg.VneImgService;
import com.artogether.venue.vneorder.VneBookingSystem;
import com.artogether.venue.vneorder.VneOrderService;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    @Autowired
    private VneBookingSystem vneBookingSystem;
    @Autowired
    private VneOrderService vneOrderService;

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
    @GetMapping("/order/availability/{vneId}")
    public AvailableDTO getAvailableDTO(@PathVariable("vneId") Integer vneId,
                                        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        LocalDateTime now = LocalDateTime.now();
        Boolean islockDate = vneBookingSystem.islockDate(vneId, date);
        System.out.println("islockDate"+islockDate);
        if (islockDate) {
            throw new VenueExceptions.DateAlreadyLockedException("The date " + date + " is already locked for venue ID " + vneId);
        }
        boolean lockDate = vneBookingSystem.lockDate(vneId, date);
        System.out.println("lockDate"+lockDate);
        AvailableDTO availableDTO = tslotService.getAvailableDTO(vneId, date, now);
        return availableDTO;
    }
    //解鎖
    @PostMapping("/order/unlock/{vneId}")
    public void unlockDate(@PathVariable("vneId") Integer vneId,
                           @RequestBody UnlockRequest unlockRequest){
        LocalDate date = unlockRequest.getDate();
        System.out.println("controller");
        vneBookingSystem.unlockDate(vneId, date);

    }

    //送出預約
    @PostMapping("/order/submit/{vneId}")
    public ResponseEntity<Object> submitOrder(@PathVariable("vneId") Integer vneId,
                                              @RequestBody VneOrderDTO vneOrderDTO,
                                              HttpSession session) {
        try {
            vneOrderDTO.setVneId(vneId);
            Integer member = (Integer) session.getAttribute("member");
            vneOrderDTO.setMemId(member);
            LocalDateTime now = LocalDateTime.now();

            VneOrderDTO orderDTO = vneOrderService.previewOrder(vneOrderDTO, now);

            return ResponseEntity.ok(orderDTO);
        } catch (VenueExceptions.DateAlreadyLockedException e) {
            // 返回 409 錯誤，並附帶詳細錯誤訊息
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    // 最終保存階段：確認保存訂單
    @PostMapping("/order/payment/full/{vneId}")
    public ResponseEntity<Map<String, Object>> confirmOrder(@PathVariable("vneId") Integer vneId,
                                               @RequestBody VneOrderDTO vneOrderDTO) {
        LocalDateTime now = LocalDateTime.now();
        vneOrderDTO.setVneId(vneId);
        System.out.println(vneOrderDTO);
        System.out.println("confirmOrder");
        Integer orderId = vneOrderService.CreateSingleDayVneOrder(vneOrderDTO, now);
        boolean created = vneOrderService.isCreated(orderId);
        System.out.println("created"+created);
        Map<String, Object> response = new HashMap<>();
        if (created) {
            response.put("success", true);
            response.put("message", "Order successfully created!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("success", false);
            response.put("message", "Insufficient payment!");
            return ResponseEntity.badRequest().body(response);
        }
    }

//    //付訂金
//    @PostMapping("/order/payment/deposit/{vneId}")
}
