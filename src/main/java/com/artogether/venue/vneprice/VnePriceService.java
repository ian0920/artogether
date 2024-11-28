package com.artogether.venue.vneprice;

import com.artogether.venue.venue.Venue;
import com.artogether.venue.vnedto.VnePriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class VnePriceService {

    @Autowired
    private VnePriceRepository vnePriceRepository;

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
                                .priceTslot(vnePriceDTO.getPriceTslot())
                                .dayOfWeek(vnePriceDTO.getDayOfWeek())
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
                                  .priceTslot(vnePriceDTO.getPriceTslot())
                                  .dayOfWeek(vnePriceDTO.getDayOfWeek())
                                  .effectiveTime(submissionTime)
                                  .build();
            return vnePriceRepository.save(newVnePrice);
        }
    }

}
