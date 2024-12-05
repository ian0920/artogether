package com.artogether.venue.vneorder;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberRepo;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueRepository;
import com.artogether.venue.vnedto.VneOrderDTO;
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

    //有個Multimap(Guava Library)可能可以用
    //取出訂單中所有預約的小時
    public Map<LocalDate, BitSet> getBookingTslot (Integer vneId) {
        List<VneOrder> vneOrders = vneOrderRepository.findUnclosedOrders(vneId,LocalDate.now());
        Map<LocalDate, BitSet> bookingTslotMap = new HashMap<>();
        for (VneOrder vneOrder : vneOrders) {
            LocalDate startDate = vneOrder.getStartDate();
            Integer startTime = vneOrder.getStartTime();
            LocalDate endDate = vneOrder.getEndDate();
            Integer endTime = vneOrder.getEndTime();
            long days = ChronoUnit.DAYS.between(startDate, endDate);
            BitSet bookingTslot = null;
            //若為單日訂單
            if (days == 0) {
                //設定為<=因為希望設定一小時的緩衝時間
                for (int i = startTime; i <= endTime; i++) {
                    bookingTslot = new BitSet();
                    bookingTslot.set(i);
                    if (bookingTslotMap.containsKey(startDate)) {
                        bookingTslotMap.get(startDate).or(bookingTslot);
                        bookingTslotMap.put(startDate, bookingTslot);
                    }
                    bookingTslotMap.put(startDate, bookingTslot);
                }
            }
        }
        return bookingTslotMap;
    }

    //找出不能預約的天數
    public List<LocalDate> getDisableDate(Integer vneId){
        Venue venue = venueRepository.findById(vneId).get();
        Integer availableDays = venue.getAvailableDays();
        List<LocalDate> openDates = venueRepository.getAvailableDates(availableDays);
        List<LocalDate> disableDates = new ArrayList<>();
        int binaryWeek = tslotService.getBinaryWeek(vneId, LocalDateTime.now());

        if (binaryWeek == 127){
            return disableDates;
        }else {
            for (LocalDate openDate : openDates) {
                int weekday = openDate.getDayOfWeek().getValue();
                if ((binaryWeek & 1 << (7-weekday))==0){
                    disableDates.add(openDate);
                }
            }
        return disableDates;
        }
    }

    public Integer CreateSingleDayVneOrder (Integer memId, Integer vneId, VneOrderDTO vneOrderDTO, LocalDateTime submissionTime) {
        VneOrder vneOrder = new VneOrder();
        Member member = memberRepository.findById(memId).get();
        Venue venue = venueRepository.findById(vneId).get();
        vneOrder.setMember(member);
        vneOrder.setVenue(venue);
        vneOrder.setOrderDate(submissionTime);

        Integer orderId = 1;
        return orderId;
    }

    public Integer CreateCrossDayVneOrder (Integer memId, Integer vneId, VneOrderDTO vneOrderDTO, LocalDateTime submissionTime) {
        Integer orderId = 1;
        return orderId;
    }

//    public Integer getTotalPrice ()
}

