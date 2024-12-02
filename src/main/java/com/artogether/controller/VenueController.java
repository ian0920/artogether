package com.artogether.controller;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.*;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.vneimg.VneImgService;
import com.artogether.venue.vneprice.VnePrice;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/venue")
public class VenueController {

    @Autowired
    private TslotService tslotService;
    @Autowired
    private VnePriceService vnePriceService;
    @Autowired
    private VneImgService vneImgService;
    @Autowired
    private VenueService venueService;

//    //若有之前檔案的話修改時會秀出來
//    @ModelAttribute("weeklyTslots")
//    public Map<String, List<Integer>> prepareWeeklyTslots(@RequestParam("vneId") Integer vneId) {
//        LocalDateTime now = LocalDateTime.now();
//        return tslotService.getWeeklyTslots(vneId, now);
//    }
//    //若有之前檔案的話修改時會秀出來
//    @ModelAttribute("vnePriceData")
//    public VnePriceDTO prepareVnePriceData(@RequestParam("vneId") Integer vneId) {
//        LocalDateTime now = LocalDateTime.now();
//        VnePriceDTO vnePriceDTO = vnePriceService.getNearestVnePrice(vneId, now);
//        return vnePriceDTO;
//    }
//    @ModelAttribute("venueDetail")
//    public VneDetailDTO getVenueDetail(@RequestParam ("vneId") int vneId) {
//        VneDetailDTO vneDetailDTO = venueService.getDetailVenue(vneId);
//        return vneDetailDTO;
//    }

    //店家場地總覽
    @GetMapping("/vneList")
    public String vneList(Model model, HttpSession session) {
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        int businessId = businessMember.getId();
        List<VneCardDTO> vneCardDTOs = venueService.bizVneList(businessId);
        model.addAttribute("vneCardDTOs", vneCardDTOs);
        return "venue/business/html/vneList";
    }
    //新增場地頁面
    @GetMapping("/addVenue")
    public String addVenue(Model model) {
        return "/venue/business/html/addVenue";
    }
    //創建新場地
    @PostMapping("/createVenue")
    public String createVenue(@ModelAttribute VneDetailDTO vneDetailDTO, HttpSession session) {
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        int businessId = businessMember.getId();
        venueService.creatVenue(vneDetailDTO, businessId);
        return "redirect:manageVenue";
    }

    //調整場地的頁面
    @GetMapping("/manageVenue")
    public String getVenueAndImg (@RequestParam("vneId") Integer vneId, Model model) {
        VneDetailDTO vneDetailDTO = venueService.getDetailVenue(vneId);
        model.addAttribute("vneDetail", vneDetailDTO);
        return "/venue/business/html/manageVenue";
    }
    //修改場地內容
    @PostMapping("/updateVenue")
    public String uploadVenue(@RequestParam Integer vneId, @ModelAttribute VneDetailDTO venDetailDTO, HttpSession session) {
        venueService.updateVenue(vneId, venDetailDTO);
        return "redirect:manageVenue";
    }
    //上傳場地圖片
//    @PostMapping("/updateImg")
//    public String manageImg( @ModelAttribute VneImgBytesDTO vneImgBytesDTO ) {
//        vneImgService.updateVneImg(vneImgBytesDTO);
//        return "redirect:manageVenue";
//    }
    @PostMapping("/updateImg")
    public String manageImg(@RequestParam("file") MultipartFile file,
                            @RequestParam("vneId") Integer vneId,
                            @RequestParam("position") Integer position) {
        try {
            VneImgBytesDTO vneImgBytesDTO = new VneImgBytesDTO();
            vneImgBytesDTO.setVneId(vneId);
            vneImgBytesDTO.setImageFile(file.getBytes());
            vneImgBytesDTO.setPosition(position);

            vneImgService.updateVneImg(vneImgBytesDTO);
            return "redirect:manageVenue";
        } catch (Exception e) {
            e.printStackTrace(); // 打印完整的異常
            return "error"; // 返回錯誤頁面或提示
        }
    }


    //營業時間設置頁面
    @GetMapping("/manageTslot")
    public String nearestTslot(@RequestParam("vneId") Integer vneId, Model model) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, List<Integer>> weeklyTslots = tslotService.getWeeklyTslots(vneId, now);
        for (Map.Entry<String, List<Integer>> daylyTslot : weeklyTslots.entrySet()) {
            model.addAttribute(daylyTslot.getKey(), daylyTslot.getValue());
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
