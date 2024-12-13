package com.artogether.venue.vneprice;

import com.artogether.util.BinaryTools;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueRepository;
import com.artogether.venue.vnedto.VnePriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VnePriceService {

    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private VnePriceRepository vnePriceRepository;
    @Autowired
    private TslotService tslotService;

    //獲取上一次的紀錄
    public VnePriceDTO getNearestVnePrice(Integer vneId, LocalDateTime submissionTime) {
        Optional<VnePrice> vnePriceOptional = vnePriceRepository.getNearestPastRecord(vneId, submissionTime);
        VnePriceDTO vnePriceDTO = new VnePriceDTO();
        vnePriceOptional.ifPresent(vnePrice -> {
            vnePriceDTO.setDefaultPrice(vnePrice.getDefaultPrice());
            Integer price = vnePrice.getPrice();
            if (price != null) {
                vnePriceDTO.setPrice(vnePrice.getPrice());
                String dayOfWeek = vnePrice.getDayOfWeek();
                List<Integer> weekdays = BinaryTools.toList(dayOfWeek);
                vnePriceDTO.setDayOfWeek(weekdays);
                String priceTslot = vnePrice.getPriceTslot();
                vnePriceDTO.setStartTime(BinaryTools.first(priceTslot));
                vnePriceDTO.setEndTime(BinaryTools.last(priceTslot));
            }
        });
        return vnePriceDTO;
    }

    // 創建或更新價錢
    @Transactional
    public void updateVnePrice(LocalDateTime submissionTime, VnePriceDTO vnePriceDTO) {
        int vneId = vnePriceDTO.getVneId();
        Optional<VnePrice> vnePriceOptional = vnePriceRepository.getNearestPastRecord(vneId, submissionTime);
        vnePriceOptional.ifPresent(vnePrice -> {
            vnePrice.setExpirationTime(submissionTime);
        });
        //不管有沒有找到都新建一個
        Integer startTime = vnePriceDTO.getStartTime();
        Integer endTime = vnePriceDTO.getEndTime();

        VnePrice.VnePriceBuilder builder = VnePrice.builder()
                .venue(Venue.id(vneId)) // Venue 有靜態方法"id()"(嘗試看看)
                .defaultPrice(vnePriceDTO.getDefaultPrice())
                .effectiveTime(submissionTime);
        Integer price = vnePriceDTO.getPrice();
        if (price != null) {
             builder.price(vnePriceDTO.getPrice())
            .priceTslot(BinaryTools.toBinaryString(BinaryTools.toBitSet(getPriceTslotList(startTime, endTime)),24))
            .dayOfWeek(BinaryTools.toBinaryString(BinaryTools.toBitSet(vnePriceDTO.getDayOfWeek()),7));
        }
        VnePrice vnePrice = builder.build();
        vnePriceRepository.save(vnePrice);
    }

    //處理前端回來的特殊價錢需間
    public List<Integer> getPriceTslotList(Integer startTime, Integer endTime) {
        List<Integer> slotList = new ArrayList<>();
        for (int i = startTime; i < endTime; i++) {
            slotList.add(i);
        }
        return slotList;
    }

    //製作可營業的時間價錢對照表
    public Map<Integer, Integer> getPriceMap (Integer vneId, LocalDate bookingDate) {
        Map<Integer, Integer> priceMap = new HashMap<>();
        VnePriceDTO vnePriceDTO = getNearestVnePrice(vneId, LocalDateTime.now());
        List<Integer> dayOfWeek = vnePriceDTO.getDayOfWeek();
        int value = bookingDate.getDayOfWeek().getValue();
        Integer defaultPrice = vnePriceDTO.getDefaultPrice();
        Integer price = vnePriceDTO.getPrice();
        BitSet availableHours = tslotService.getAvailableBitSet(vneId, bookingDate);

        if (dayOfWeek.contains(value)) {
            if (price != null) {
                int start = availableHours.nextSetBit(0);
                while (start != -1) {
                    priceMap.put(start, defaultPrice);
                    start = availableHours.nextSetBit(start + 1);
                }
                return priceMap;
            }else {
                Integer startTime = vnePriceDTO.getStartTime();
                Integer endTime = vnePriceDTO.getEndTime();
                BitSet specialPriceList = BinaryTools.toBitSet(getPriceTslotList(startTime, endTime));
                int start = availableHours.nextSetBit(0);
                while (start != -1) {
                    if (specialPriceList.get(start)) {
                        priceMap.put(start, price);
                    }else {
                        priceMap.put(start, defaultPrice);
                    }
                    start = availableHours.nextSetBit(start + 1);
                }
                return priceMap;
            }
        }else {
            int start = availableHours.nextSetBit(0);
            while (start != -1) {
                priceMap.put(start, defaultPrice);
                start = availableHours.nextSetBit(start + 1);
            }
            return priceMap;
        }
    }
}
