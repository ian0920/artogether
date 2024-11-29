package com.artogether.controller;

import com.artogether.venue.vnedto.TslotDTO;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.vnedto.VenueCreatDTO;
import com.artogether.venue.vnedto.VneImgBytesDTO;
import com.artogether.venue.vnedto.VnePriceDTO;
import com.artogether.venue.vneimg.VneImgService;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    public String manageImg(@Valid @ModelAttribute VneImgBytesDTO vneImgBytesDTO, BindingResult bindingResult) {
        vneImgService.updateVneImg(vneImgBytesDTO);
        return "redirect:/managevenue";
    }

    @PostMapping("/creatVenue")
    public String creatVenue (@ModelAttribute VenueCreatDTO vvenueCreatDTO, HttpSession session) {
//        Integer businessId = session.getAttribute(businessId);

        return "redirect:/managevenue";
    }
}
