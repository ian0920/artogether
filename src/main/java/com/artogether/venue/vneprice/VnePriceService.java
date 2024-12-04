package com.artogether.venue.vneprice;

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

    public VnePriceDTO getNearestVnePrice(Integer vneId, LocalDateTime submissionTime) {
        VnePrice vnePrice = vnePriceRepository.getNearestPastRecord(submissionTime);
        VnePriceDTO vnePriceDTO = new VnePriceDTO();
        Optional<Venue> venueOptional = venueRepository.findById(vneId);
        Venue venue = venueOptional.get();
        vnePriceDTO.setVneName(venue.getName());
        if (vnePrice != null) {
            vnePriceDTO.setDefaultPrice(vnePrice.getDefaultPrice());
            String dayOfWeek = vnePrice.getDayOfWeek();
            if (dayOfWeek != null) {
                vnePriceDTO.setPrice(vnePrice.getPrice());
                List<Integer> weekdays = new ArrayList<>();
                for (int i = 1; i < 8; i++) {
                    if (dayOfWeek.charAt(i) == '1') {
                        weekdays.add(i);
                    }
                }
                vnePriceDTO.setDayOfWeek(weekdays);
                String priceTslot = vnePrice.getPriceTslot();
                List<Integer> timeToPrice = new ArrayList<>();
                if (priceTslot != null) {
                    timeToPrice = tslotService.getDaylyTslots(priceTslot);
                    vnePriceDTO.setPriceTslot(timeToPrice);
                    return vnePriceDTO;
                }
            }else {
                return vnePriceDTO;
            }
        }
            return vnePriceDTO;
    }
    // 創建或更新價錢
    @Transactional
    public VnePrice updateVnePrice(LocalDateTime submissionTime, VnePriceDTO vnePriceDTO) {
        int vneId = vnePriceDTO.getVneId();
        if (!vnePriceRepository.existsByVenueId(vneId)) {
            //沒有找到新建一個
            VnePrice vnePrice = VnePrice.builder()
                                .venue(Venue.id(vneId)) // Venue 有靜態方法"id()"(嘗試看看)
                                .defaultPrice(vnePriceDTO.getDefaultPrice())
                                .price(vnePriceDTO.getPrice())
//                                .priceTslot(vnePriceDTO.getPriceTslot())
//                                .dayOfWeek(vnePriceDTO.getDayOfWeek())
                                .effectiveTime(submissionTime)
                                .build();
            return vnePriceRepository.save(vnePrice);
        }else {
            //找到最靠近現在的前一筆資料寫入更改時間
            VnePrice vnePrice = new VnePrice();
            vnePrice = vnePriceRepository.getNearestPastRecord(submissionTime);
            vnePriceRepository.save(vnePrice);

            //有找到舊的所以另存一個新的
            VnePrice newVnePrice = VnePrice.builder()
                                  .venue(Venue.id(vneId)) // Venue 有靜態方法"id()"(嘗試看看)
                                  .defaultPrice(vnePriceDTO.getDefaultPrice())
                                  .price(vnePriceDTO.getPrice())
//                                  .priceTslot(vnePriceDTO.getPriceTslot())
//                                  .dayOfWeek(vnePriceDTO.getDayOfWeek())
                                  .effectiveTime(submissionTime)
                                  .build();
            return vnePriceRepository.save(newVnePrice);
        }
    }

}
