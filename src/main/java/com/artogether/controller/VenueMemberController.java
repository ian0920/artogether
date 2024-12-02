package com.artogether.controller;

import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.VneDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/venue/mem")
public class VenueMemberController {

    @Autowired
    private VenueService venueService;

    @GetMapping("detail")
    public String detail(@RequestParam("vneId")Integer vneId, Model model) {
        VneDetailDTO vneDetailDTO = venueService.getDetailVenue(vneId);
        model.addAttribute("vneDetail", vneDetailDTO);
        return "/venue/member/html/detail";
    }
}
