package com.artogether.venue.vneorder;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberRepo;
import com.artogether.venue.VenueExceptions;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueRepository;
import com.artogether.venue.vnedto.AvailableDTO;
import com.artogether.venue.vnedto.VneOrderDTO;
import com.artogether.venue.vneprice.VnePriceService;
import groovy.util.MapEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private VnePriceService vnePriceService;
    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private VneBookingSystem vneBookingSystem;

    //驗證
    public VneOrderDTO previewOrder(VneOrderDTO vneOrderDTO, LocalDateTime submissionTime) {
        Integer vneId = vneOrderDTO.getVneId();
        LocalDate startDate = vneOrderDTO.getStartDate();
        AvailableDTO availableDTO = tslotService.getAvailableDTO(vneId, startDate, submissionTime);
        List<List<Integer>> availableSegments = availableDTO.getAvailableSegments();
        Integer startTime = vneOrderDTO.getStartTime();
        Integer endTime = vneOrderDTO.getEndTime();
        List<Integer> tslotList = vnePriceService.getTslotList(startTime, endTime);
        Boolean isContained = availableSegments.stream().anyMatch(segment -> segment.containsAll(tslotList));
        if (isContained) {
            vneBookingSystem.lockedByBooking(vneId,startDate);
            Integer totalPrice = 0;
            Map<Integer, Integer> hourlyPrice = availableDTO.getHourlyPrice();
            for (Integer hour: tslotList) {
                totalPrice += hourlyPrice.get(hour);
            }
            vneOrderDTO.setTotalPrice(totalPrice);
//            Integer memId = vneOrderDTO.getMemId();
//            System.out.println(memId);
//            memberRepo.findById(memId).ifPresent(member -> {
//                vneOrderDTO.setMemName(member.getName());
//                vneOrderDTO.setMemPhone(member.getPhone());
//            });
            return vneOrderDTO;
        }else {
            throw new VenueExceptions.DateAlreadyLockedException("訂單送出過程出了意外?!");
        }
    }
    public boolean isCreated(Integer orderId) {
        Optional<VneOrder> vneOrderOptional = vneOrderRepository.findById(orderId);
        if (vneOrderOptional.isPresent()) {
            return true;
        }else {return false;}
    }
    public Integer CreateSingleDayVneOrder (VneOrderDTO vneOrderDTO, LocalDateTime submissionTime) {
        VneOrder vneOrder = new VneOrder();
        Integer memId = vneOrderDTO.getMemId();
        Integer vneId = vneOrderDTO.getVneId();
        Member member = memberRepository.findById(memId).get();
        Venue venue = venueRepository.findById(vneId).get();
        vneOrder.setMember(member);
        vneOrder.setVenue(venue);
        vneOrder.setOrderDate(submissionTime);
        Integer totalPrice = vneOrderDTO.getTotalPrice();
        Integer paid = vneOrderDTO.getPaid();
        vneOrder.setTotalPrice(totalPrice);
//        vneOrder.getVneCoupId();
        vneOrder.setShouldPaid(totalPrice-paid);
        vneOrder.setPaid(paid);
        vneOrder.setStatus(OrderStatusEnum.PAID_FULL);
        vneOrder.setStartDate(vneOrderDTO.getStartDate());
        vneOrder.setStartTime(vneOrderDTO.getStartTime());
        vneOrder.setEndDate(vneOrderDTO.getStartDate());
        vneOrder.setEndTime(vneOrderDTO.getEndTime());
        vneOrderRepository.save(vneOrder);
        Integer orderId = vneOrder.getId();
        return orderId;
    }

    //裝DTO
    public VneOrderDTO getOrderDTO(VneOrder vneOrder) {
        Member member = vneOrder.getMember();
        Venue venue = vneOrder.getVenue();
        VneOrderDTO vneOrderDTO = VneOrderDTO.builder()
                .vneId(venue.getId())
                .memId(member.getId())
                .memName(member.getName())
                .memPhone(member.getPhone())
                .vneName(venue.getName())
                .bizAddress(venue.getBusinessMember().getAddr())
                .orderId(vneOrder.getId())
                .totalPrice(vneOrder.getTotalPrice())
                .shouldPaid(vneOrder.getShouldPaid())
                .paid(vneOrder.getPaid())
                .startDate(vneOrder.getStartDate())
                .startTime(vneOrder.getStartTime())
                .endDate(vneOrder.getEndDate())
                .endTime(vneOrder.getEndTime()).build();
        return vneOrderDTO;
    }

    public List<VneOrderDTO> getMemOrderList(Integer memId) {
        List<VneOrder> byMemberId = vneOrderRepository.findByMember_Id(memId);
        List<VneOrderDTO> memList = byMemberId.stream()
                .map(this::getOrderDTO)
                .collect(Collectors.toList());

        return memList;
    }
    public List<VneOrderDTO> getVneOrderList(Integer vneId) {
        List<VneOrder> byMemberId = vneOrderRepository.findByVenue_Id(vneId);
        List<VneOrderDTO> memList = byMemberId.stream()
                .map(this::getOrderDTO)
                .collect(Collectors.toList());

        return memList;
    }
//    public Integer CreateCrossDayVneOrder (Integer memId, Integer vneId, VneOrderDTO vneOrderDTO, LocalDateTime submissionTime) {

}

