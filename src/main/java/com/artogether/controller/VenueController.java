package com.artogether.controller;

import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.TslotDTO;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.vnedto.VneDetailDTO;
import com.artogether.venue.vnedto.VneImgBytesDTO;
import com.artogether.venue.vnedto.VnePriceDTO;
import com.artogether.venue.vneimg.VneImgService;
import com.artogether.venue.vneprice.VnePrice;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("/venue")
public class VenueController {

    @Autowired
    private TslotService tslotService;
    @Autowired
    private VnePriceService vnePriceService;
    @Autowired
    private VneImgService vneImgService;
    @Autowired
    private VenueService venueService;

    @PostMapping("/creatVenue")
    public String creatVenue (@ModelAttribute VneDetailDTO venDetailDTO, HttpSession session) {
//        Integer businessId = session.getAttribute(businessId);
        int businessId = 1;
        venueService.creatVenue(venDetailDTO, businessId);
        return "redirect:/managevenue";
    }

    @GetMapping("/manageTslot")
    public String nearestTslot (@RequestParam("vneId")Integer vneId, Model model, HttpSession session) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, List<Integer>> weeklyTslots = tslotService.getWeeklyTslots(vneId, now);
        //foreach不能直接取兩個值，課堂中教的方法是Map.Entry，所以集合那一側也要加上".entrySet()"
        //for(Map.Entry<String, List<Integer>> entry : weeklyTslots.entrySet()) {}
        //java8以後有處理map的foreach()
        weeklyTslots.forEach((day, daylyTslots) -> {
            model.addAttribute(day, daylyTslots);
        });
        return "manageTslot";
    }

    @PostMapping("/manageTslot")
    public String manageTslot(@ModelAttribute TslotDTO tslotDTO) {
        LocalDateTime submissionTime = LocalDateTime.now();
        tslotService.updateTslot(submissionTime, tslotDTO);
        // 進行處理，例如存入資料庫
        return "redirect:/managevenue";
    }

    @GetMapping("/managePrice")
    public String nearestPrice (@RequestParam("vneId")Integer vneId, Model model, HttpSession session) {
        LocalDateTime now = LocalDateTime.now();
        VnePriceDTO vnePriceDTO = vnePriceService.getNearestVnePrice(vneId, now);
        model.addAttribute("vneId", vneId);
        model.addAttribute("vneName", vnePriceDTO.getVneName());
        Integer defaultPrice = vnePriceDTO.getDefaultPrice();
        Integer price = vnePriceDTO.getPrice();
        if (defaultPrice != null) {
            model.addAttribute("defaultPrice", defaultPrice);
            if (price != null) {
            model.addAttribute("price", price);
            model.addAttribute("dayOfWeek", vnePriceDTO.getDayOfWeek());
            List<Integer> priceTslots = vnePriceDTO.getPriceTslot();
                if (priceTslots != null) {
                    model.addAttribute("priceTslots", priceTslots);
                }
            }
        }
        return "managePrice";
    }

    @PostMapping("/managePrice")
    public String managePrice(@ModelAttribute VnePriceDTO vnePriceDTO) {
        LocalDateTime submissionTime = LocalDateTime.now();
        vnePriceService.updateVnePrice(submissionTime, vnePriceDTO);
        return "redirect:/managevenue";
    }

    @GetMapping("manageImg")
    public  String tryupload(Model model, HttpSession session) {
//        (@RequestParam Integer vneId, Model model, HttpSession session)
        return "redirect:venue/html/test4";
    }
    @PostMapping("/manageImg")
    public String manageImg(@Valid @ModelAttribute VneImgBytesDTO vneImgBytesDTO, BindingResult bindingResult) {
        vneImgService.updateVneImg(vneImgBytesDTO);
        return "redirect:/managevenue";
    }



}
