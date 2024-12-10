package com.artogether.venue.vneorder;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberRepo;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueRepository;
import com.artogether.venue.vnedto.VneOrderDTO;
import groovy.util.MapEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class VneOrderService {

    @Autowired
    private MemberRepo memberRepository;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private VneOrderRepository vneOrderRepository;
    @Autowired
    private TslotService tslotService;

    public Integer CreateSingleDayVneOrder (Integer memId, Integer vneId, VneOrderDTO vneOrderDTO, LocalDateTime submissionTime) {
        VneOrder vneOrder = new VneOrder();
        Member member = memberRepository.findById(memId).get();
        Venue venue = venueRepository.findById(vneId).get();
        vneOrder.setMember(member);
        vneOrder.setVenue(venue);
        vneOrder.setOrderDate(submissionTime);
        vneOrder.setTotalPrice(vneOrderDTO.getTotalPrice());
//        vneOrder.getVneCoupId();
        vneOrder.setShouldPaid(vneOrderDTO.getShouldPaid());
        vneOrder.setPaid(vneOrderDTO.getPaid());
        vneOrder.setStartDate(vneOrderDTO.getStartDate());
        vneOrder.setStartTime(vneOrderDTO.getStartTime());
        vneOrder.setEndDate(vneOrderDTO.getEndDate());
        vneOrder.setEndTime(vneOrderDTO.getEndTime());
        Integer orderId = 1;
        return orderId;
    }

    public Integer CreateCrossDayVneOrder (Integer memId, Integer vneId, VneOrderDTO vneOrderDTO, LocalDateTime submissionTime) {
        Integer orderId = 1;
        return orderId;
    }

//    public Integer getTotalPrice ()
}

