package com.artogether.venue.vneprice;

import com.artogether.util.BinaryTools;
import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueRepository;
import com.artogether.venue.vnedto.VnePriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VnePriceService {

    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private VnePriceRepository vnePriceRepository;
    @Autowired
    private TslotService tslotService;

    //修改時顯示
    public VnePriceDTO getNearestVnePrice(Integer vneId, LocalDateTime submissionTime) {
        Optional<VnePrice> vnePriceOptional = vnePriceRepository.getNearestPastRecord(vneId, submissionTime);
        VnePriceDTO vnePriceDTO = new VnePriceDTO();
//        venueRepository.findById(vneId).ifPresent(venue -> {
//            String name = venue.getName();
//            vnePriceDTO.setVneName(name);
//        });
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
                .defaultPrice(vnePriceDTO.getDefaultPrice());
        Integer price = vnePriceDTO.getPrice();
        if (price != null) {
             builder.price(vnePriceDTO.getPrice())
            .priceTslot(BinaryTools.toBinaryString(getPriceTslotList(startTime, endTime), 24))
            .dayOfWeek(BinaryTools.toBinaryString(vnePriceDTO.getDayOfWeek(), 7));
        }
        VnePrice vnePrice = builder.build();
        vnePriceRepository.save(vnePrice);
    }

    public List<Integer> getPriceTslotList(Integer startTime, Integer endTime) {
        List<Integer> slotList = new ArrayList<>();
        for (int i = startTime; i < endTime; i++) {
            slotList.add(i);
        }
        return slotList;
    }
}
