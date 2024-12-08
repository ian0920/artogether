package com.artogether.controller.venue;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.util.BinaryTools;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.*;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.vneimg.VneImgService;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    //店家場地總覽
    @GetMapping("/vneList")
    public String vneList() {
        //交給Ajax->/vneBiz/vneList
        return "venue/business/html/vneList";
    }
    //新增場地頁面
    @GetMapping("/addVenue")
    public String addVenue() {
        return "/venue/business/html/addVenue";
    }
    //創建新場地
    @PostMapping("/createVenue")
    public String createVenue(@ModelAttribute VneDetailDTO vneDetailDTO, HttpSession session) {
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        Integer vneId = venueService.createVenue(vneDetailDTO, businessMember);
        return "redirect:/vneBiz/manageVenue/"+vneId;
    }

    //調整場地的頁面
    @GetMapping("/manageVenue/{vneId}")
    public String getVenueAndImg () {
        return "venue/business/html/manageVenue";
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
//    @PostMapping("/updateImg")
//    public String manageImg( @ModelAttribute VneImgBytesDTO vneImgBytesDTO ) {
//        vneImgService.updateVneImg(vneImgBytesDTO);
//        return "redirect:manageVenue";
//    }
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
    @GetMapping("/manageTslot")
    public String nearestTslot(@RequestParam("vneId") Integer vneId, Model model) {
        LocalDateTime now = LocalDateTime.now();
        List<Integer> weeklyTslots = tslotService.getWeeklyTslots(vneId, now);
        for (int i = 0; i <= weeklyTslots.size()-1; i++) {
            List<Integer> daylyTslot = BinaryTools.toList(weeklyTslots.get(i),24);
            int dayOfWeek = 1;
            dayOfWeek += i;
            String dayName = DayOfWeek.of(dayOfWeek).toString();
            model.addAttribute(dayName, daylyTslot);
        }
        return "/venue/business/html/manageTslot";
    }
    //調整營業時間
    @PostMapping("/updateTslot")
    public String manageTslot(@ModelAttribute TslotDTO tslotDTO) {
        LocalDateTime submissionTime = LocalDateTime.now();
        tslotService.updateTslot(submissionTime, tslotDTO);
        return "redirect:manageTslot";
    }
    //價錢設置頁面
    @GetMapping("/managePrice")
    public String nearestPrice() {
        return "/venue/business/html/managePrice";
    }
    //調整價錢
    @PostMapping("/updatePrice")
    public String managePrice(@ModelAttribute VnePriceDTO vnePriceDTO) {
        LocalDateTime submissionTime = LocalDateTime.now();
        vnePriceService.updateVnePrice(submissionTime, vnePriceDTO);
        return "redirect:managePrice";
    }
    //場地總覽，確認後可以上下架
    @GetMapping("/checkVenue")
    public String checkVenue() {
        return "/venue/business/html/checkVenue";
    }
    //調整上下架
    @PostMapping("/venueStatus")
    public String manageVenue(@ModelAttribute VneDetailDTO venDetailDTO, HttpSession session) {
        return "redirect:list";
    }
    //測試用
    @GetMapping("/manageImg")
    public  String tryupload(Model model, HttpSession session) {
//        (@RequestParam Integer vneId, Model model, HttpSession session)
        return "/venue/business/html/test4";
    }



}
