package com.artogether.venue.tslot;

import com.artogether.venue.venue.Venue;
import com.artogether.venue.vnedto.TslotDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TslotService {

    @Autowired
    private TslotRepository tslotRepository;

    @Autowired
    private TslotViewService tslotViewService;

    //只在第一次啟動時執行。
    @PostConstruct
//    @Transactional
    public void init() {
        tslotViewService.createTslotScheduleView();
    }


    // 創建或更新時段
    @Transactional
    public Tslot updateTslot(LocalDateTime submissionTime, TslotDTO tslotDTO) {
        int vneId = tslotDTO.getVneId();
        if (!tslotRepository.existsByVenueId(vneId)) {
            //沒有找到新建一個
            Tslot tslot = Tslot.builder()
                    .venue(Venue.id(vneId)) // Venue 有靜態方法"id()"(嘗試看看)
                    .hourOfMon(tslotDTO.getHourOfMon())
                    .hourOfTue(tslotDTO.getHourOfTue())
                    .hourOfWed(tslotDTO.getHourOfWed())
                    .hourOfThu(tslotDTO.getHourOfThu())
                    .hourOfFri(tslotDTO.getHourOfFri())
                    .hourOfSat(tslotDTO.getHourOfSat())
                    .hourOfSun(tslotDTO.getHourOfSun())
                    .effectiveTime(submissionTime)
                    .build();

            return tslotRepository.save(tslot);
        }else {
            //找到最靠近現在的前一筆資料寫入更改時間
            Tslot tslot = new Tslot();
            tslot = tslotRepository.getNearestPastRecord(submissionTime);
            tslotRepository.save(tslot);

            //有找到舊的所以另存一個新的
            Tslot newTslot = Tslot.builder()
                .venue(Venue.id(vneId)) // Venue 有靜態方法"id()"(嘗試看看)
                .hourOfMon(tslotDTO.getHourOfMon())
                .hourOfTue(tslotDTO.getHourOfTue())
                .hourOfWed(tslotDTO.getHourOfWed())
                .hourOfThu(tslotDTO.getHourOfThu())
                .hourOfFri(tslotDTO.getHourOfFri())
                .hourOfSat(tslotDTO.getHourOfSat())
                .hourOfSun(tslotDTO.getHourOfSun())
                .expirationTime(submissionTime)
                .build();
            return tslotRepository.save(newTslot);
        }
    }

    // 讀取所有時段
    public List<Tslot> getAllTslots() {
        return tslotRepository.findAll();
    }

    // 讀取單一時段
    // Optional 是 Java 8 引入的一個容器類，用於表示可能包含也可能不包含非空值的容器。
    public Optional<Tslot> getTslotById(int id) {
        return tslotRepository.findById(id);
    }

//    private void setHourStr(Timestamp timestamp) {
//        // 設置分鐘、秒數為0
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        Timestamp hourTimestamp = new Timestamp(calendar.getTimeInMillis());
//        System.out.println("當前小時的 Timestamp: " + hourTimestamp);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
//        String hourString = sdf.format(hourTimestamp);
//        System.out.println("格式化到小時: " + hourString);
//    }
}
