package com.artogether.product.prd_track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Controller
public class PrdTrackController {

    private final PrdTrackService prdTrackService;

    @Autowired
    public PrdTrackController (PrdTrackService prdTrackService){
        this.prdTrackService = prdTrackService;
    }

    @PostMapping("/add")
    public PrdTrack addPrdTrack(@RequestBody PrdTrack prdTrack){
        return prdTrackService.addPrdTrack(prdTrack);
    }

    @GetMapping("/member/{memberId}")
    public List<PrdTrack> getPrdTrackByMemberId(Integer memberId){
        return prdTrackService.getPrdTrackByMemberId(memberId);
    }

    @GetMapping("/available/{memberId}")
    public List<PrdTrack> getAvailablePrdByMemberId(Integer memberId){
        return prdTrackService.getAvailablePrdByMemberId(memberId);
    }

    @GetMapping("/unavailable/{memberId}")
    public List<PrdTrack> getUnavailablePrdByMemberId(Integer memberId){
        return prdTrackService.getUnavailablePrdByMemberId(memberId);
    }

    @DeleteMapping("/remove")
    public void removeTrackPrd(PrdTrack.PrdTrackId prdTrackId){
        prdTrackService.removeTrackPrd(prdTrackId);
    }

    @DeleteMapping("/removeAll/{memberId}")
    public void removeAllTrack(Integer memberId){
        prdTrackService.removeAllTrack(memberId);
    }
}
