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

    //獲取上一次的紀錄
    public VnePriceDTO getNearestVnePrice(Integer vneId, LocalDateTime submissionTime) {
        // 確認是否有設定價錢
        VnePrice vnePrice = vnePriceRepository.getNearestPastRecord(vneId, submissionTime).orElseGet(()->{
            return null; // 如果不存在，回傳 null
            });
        VnePriceDTO vnePriceDTO = new VnePriceDTO();
        if (vnePrice != null) {
            vnePriceDTO.setDefaultPrice(vnePrice.getDefaultPrice());
            vnePriceDTO.setVneId(vneId);
            Integer price = vnePrice.getPrice();
            if (price != null) {
                vnePriceDTO.setPrice(vnePrice.getPrice());
                String dayOfWeek = vnePrice.getDayOfWeek();
                List<Integer> weekdays = BinaryTools.toList(dayOfWeek);
                vnePriceDTO.setDayOfWeek(weekdays);
                String priceTslot = vnePrice.getPriceTslot();
                if (priceTslot != null) {
                vnePriceDTO.setStartTime(BinaryTools.first(priceTslot));
                vnePriceDTO.setEndTime(BinaryTools.last(priceTslot));
                }
            }
        return vnePriceDTO;
        }else {return vnePriceDTO;}
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
            .priceTslot(BinaryTools.toBinaryString(BinaryTools.toBitSet(getTslotList(startTime, endTime)),24))
            .dayOfWeek(BinaryTools.toBinaryString(BinaryTools.toBitSet(vnePriceDTO.getDayOfWeek()),7));
        }
        VnePrice vnePrice = builder.build();
        vnePriceRepository.save(vnePrice);
    }

    //處理前端回來的特殊價錢需間
    public List<Integer> getTslotList(Integer startTime, Integer endTime) {
        List<Integer> slotList = new ArrayList<>();
        for (int i = startTime; i < endTime; i++) {
            slotList.add(i);
        }
        return slotList;
    }


}
