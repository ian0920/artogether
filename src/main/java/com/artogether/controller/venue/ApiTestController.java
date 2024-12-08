package com.artogether.controller.venue;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.venue.venue.VenueService;
import com.artogether.venue.vnedto.VneCardDTO;
import com.artogether.venue.vneimg.VneImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/vne")
public class ApiTestController {

    @Autowired
    private VneImgService vneImgService;
    @Autowired
    private VenueService venueService;


    @GetMapping("/vneList")
    public List<VneCardDTO> vneListApi(HttpSession session) {
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        int businessId = businessMember.getId();
        List<VneCardDTO> vneCardDTOs = venueService.bizVneList(businessId);
        return vneCardDTOs;
    }

    @GetMapping("/vne")
    public VneCardDTO vne() {
        Integer vneId = 1;
        VneCardDTO vneCardDTO = new VneCardDTO();
        vneCardDTO = venueService.getVenue(vneId);
        return vneCardDTO;
    }

    @PostMapping("/test1")
    public List<VneCardDTO> testApi1() {
        int businessId = 1;
        List<VneCardDTO> vneCardDTOs = venueService.bizVneList(businessId);
        return vneCardDTOs;
    }
}
