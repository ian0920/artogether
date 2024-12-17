package com.artogether.venue.vne_track;

import com.artogether.common.member.MemberRepo;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueRepository;
import com.artogether.venue.vneorder.VneBookingSystem;
import com.artogether.venue.vneorder.VneOrder;
import com.artogether.venue.vneorder.VneOrderRepository;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class VneSearchService {

    @Autowired
    private MemberRepo memberRepository;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private VneOrderRepository vneOrderRepository;
    @Autowired
    private TslotService tslotService;
    @Autowired
    private VnePriceService vnePriceService;
    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private VneBookingSystem vneBookingSystem;

    public List<VneOrder> findVneAccounting(Integer businessId, String startDate, String endDate) {


        List<Venue> vneList = venueRepository.findByBusinessMember_Id(businessId);
        List<Integer> vneIdToBusinessMember = new ArrayList<>();
        vneList.forEach(e -> vneIdToBusinessMember.add(e.getId()));

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


        LocalDateTime formatedStartDate = LocalDateTime.parse(startDate,formatter);
//        System.out.println(formatedStartDate);
        LocalDateTime formatedEndDate = LocalDateTime.parse(endDate, formatter);
//        System.out.println(formatedEndDate);

        List<VneOrder> orders = vneOrderRepository.findAllByVenue_idInAndOrderDateBetween(vneIdToBusinessMember, formatedStartDate, formatedEndDate);

//        System.out.println(orderDTOList.size());

        return orders;
    }

}
