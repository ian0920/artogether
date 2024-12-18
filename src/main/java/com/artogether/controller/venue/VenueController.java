package com.artogether.controller.venue;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.*;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.vneimg.VneImgService;
import com.artogether.venue.vneorder.VneOrderService;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/vneBiz")
public class VenueController {

    @Autowired
    private TslotService tslotService;
    @Autowired
    private VnePriceService vnePriceService;
    @Autowired
    private VneImgService vneImgService;
    @Autowired
    private VenueService venueService;
    @Autowired
    private VneOrderService vneOrderService;

    //店家場地總覽
    @GetMapping("/vneList")
    public String vneList(Model model, HttpSession session) {
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        String name = businessMember.getName();
        model.addAttribute("bizName", name);
        //交給Ajax->/vne/vneList
        return "venue/business/vneList";
    }
    //去訂單列表的
    @GetMapping("/vneList/toOrder")
    public String vneListToOrder(Model model, HttpSession session) {
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        String name = businessMember.getName();
        model.addAttribute("bizName", name);
        //交給Ajax->/vne/vneList
        return "venue/business/vneListToOrder";
    }
    //場地訂單列表
    @GetMapping("/orders/{vneId}")
    public String orderMemList(Model model, @PathVariable Integer vneId) {
        List<VneOrderDTO> memOrderList = vneOrderService.getVneOrderList(vneId);
        model.addAttribute("orders", memOrderList);
        return "/venue/business/orderList";
    }
    //新增場地頁面
    @GetMapping("/addVenue")
    public String addVenue(Model model, HttpSession session) {
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        String name = businessMember.getName();
        model.addAttribute("bizName", name);
        return "/venue/business/addVenue";
    }
    //創建新場地
    @PostMapping("/createVenue")
    public String createVenue(@ModelAttribute VneCardDTO vneCardDTO, HttpSession session) {
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        Integer vneId = venueService.createVenue(vneCardDTO, businessMember);
        return "redirect:/vneBiz/manageVenue/"+vneId;
    }

    //調整場地的頁面
    @GetMapping("/manageVenue/{vneId}")
    public String getVenueAndImg (Model model, @PathVariable Integer vneId) {
        venueService.setName(model, vneId);
        return "venue/business/manageVenue";
    }
    //修改場地內容
    @PostMapping("/updateVenue/{vneId}")
    public String uploadVenue(@PathVariable Integer vneId, @RequestBody VneDetailDTO venDetailDTO, HttpSession session) {
        System.out.println("uploadVenue");
        Venue venue =venueService.updateVenue(vneId, venDetailDTO);
        System.out.println(venue);
        return "redirect:/vneBiz/manageVenue/"+vneId;
    }
    //上傳場地圖片
    @PostMapping("/updateImg/{vneId}")
    public String manageImg(@RequestParam("file") MultipartFile file,
                            @PathVariable("vneId") Integer vneId,
                            @RequestParam("position") Integer position) {
        try {
            VneImgBytesDTO vneImgBytesDTO = new VneImgBytesDTO();
            vneImgBytesDTO.setVneId(vneId);
            System.out.println(vneId);
            vneImgBytesDTO.setImageFile(file.getBytes());
            vneImgBytesDTO.setPosition(position);
            System.out.println(position);

            vneImgService.updateVneImg(vneImgBytesDTO);
            return "redirect:/vneBiz/manageVenue/"+vneId;
        } catch (Exception e) {
            e.printStackTrace(); // 打印完整的異常
            return "error"; // 返回錯誤頁面或提示
        }
    }
    //營業時間設置頁面
    @GetMapping("/manageTslot/{vneId}")
    public String nearestTslot(Model model, @PathVariable Integer vneId) {
        venueService.setName(model, vneId);
        return "/venue/business/manageTslot";
    }
    //調整營業時間
    @PostMapping("/updateTslot/{vneId}")
    public String manageTslot(@PathVariable Integer vneId, @RequestBody TslotDTO tslotDTO) {
        System.out.println("manageTslot");
        LocalDateTime submissionTime = LocalDateTime.now();
        tslotDTO.setVneId(vneId);
        System.out.println(tslotDTO);
        tslotService.updateTslot(submissionTime, tslotDTO);
        return "redirect:/vneBiz/managePrice/"+vneId;
    }
    //價錢設置頁面
    @GetMapping("/managePrice/{vneId}")
    public String managePrice(Model model, @PathVariable Integer vneId) {
        venueService.setName(model, vneId);
        return "/venue/business/managePrice";
    }
    //調整價錢
    @PostMapping("/updatePrice/{vneId}")
    public String managePrice(@PathVariable Integer vneId, @RequestBody VnePriceDTO vnePriceDTO) {
        LocalDateTime submissionTime = LocalDateTime.now();
        vnePriceDTO.setVneId(vneId);
        System.out.println(vnePriceDTO);
        vnePriceService.updateVnePrice(submissionTime, vnePriceDTO);
        return "redirect:/vneBiz/managePrice/"+vneId;
    }
    //場地總覽，確認後可以上下架
    @GetMapping("/checkVenue/{vneId}")
    public String checkVenue(Model model, @PathVariable Integer vneId) {
        venueService.setName(model, vneId);
        return "/venue/business/checkVenue";
    }
    //調整上下架
    @PostMapping("/vneStatus")
    public String manageVenue(@ModelAttribute VneDetailDTO venDetailDTO, HttpSession session) {
        return "redirect:list";
    }
}
