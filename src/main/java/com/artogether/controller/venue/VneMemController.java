package com.artogether.controller.venue;

import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.VneDetailDTO;
import com.artogether.venue.vneimg.VneImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/vneMem")
public class VneMemController {

    @Autowired
    private VneImgService vneImgService;

    @GetMapping("/details/{vneId}")
    public String detail(@PathVariable("vneId") Integer vneId,
                         Model model) {
        vneImgService.getAllImgs(vneId);
        model.addAttribute("vneImgs", vneImgService.getAllImgs(vneId));
        return "venue/member/detail";}

    @GetMapping("/booking/{vneId}")
    public String order() {return "/venue/member/order";}
}
