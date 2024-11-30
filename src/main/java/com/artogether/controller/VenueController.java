package com.artogether.controller;

import com.artogether.venue.vnedto.TslotDTO;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.vnedto.VneImgDTO;
import com.artogether.venue.vnedto.VnePriceDTO;
import com.artogether.venue.vneimg.VneImgService;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller("/venue")
public class VenueController {

    @Autowired
    private TslotService tslotService;
    @Autowired
    private VnePriceService vnePriceService;
    @Autowired
    private VneImgService vneImgService;

    @PostMapping("/manageTslot")
    public String manageTslot(@ModelAttribute TslotDTO tslotDTO) {
        LocalDateTime submissionTime = LocalDateTime.now();
        tslotService.updateTslot(submissionTime, tslotDTO);
        // 進行處理，例如存入資料庫
        return "redirect:/managevenue";
    }

    @PostMapping("/managePrice")
    public String managePrice(@ModelAttribute VnePriceDTO vnePriceDTO) {
        LocalDateTime submissionTime = LocalDateTime.now();
        vnePriceService.updateVnePrice(submissionTime, vnePriceDTO);
        return "redirect:/managevenue";
    }

    @PostMapping("/manageImg")
    public String manageImg(@ModelAttribute VneImgDTO vneImgDTO) {
        vneImgService.updateVneImg(vneImgDTO);
        return "redirect:/managevenue";
    }

}
