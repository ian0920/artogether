package com.artogether.controller.ian;

import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.VneCardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("venues")
public class VenuesListController {

    @Autowired
    VenueService venueService;

    @GetMapping("/all")
    public String venuesList(Model model) {


        List<VneCardDTO> vneCardDTOList =  venueService.getAllVneCard();



        model.addAttribute("vneCardDTOs", vneCardDTOList);

        return "venue/venues";
    }


}
