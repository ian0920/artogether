package com.artogether.venue.vneorder;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VneBookingSystem {
    //ConcurrentHashMap用來處理多線程
//    private final ConcurrentHashMap<LocalDate,Long> lockedDates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<LocalDate, Long>> vneLocks = new ConcurrentHashMap<>();
    //以毫秒為單位，這裡設置為 5 分鐘
    private static final Long LOCK_TIMEOUT =5*60*1000L;

    public boolean lockDate(Integer vneId, LocalDate date) {
        Long currentTime = System.currentTimeMillis();
        Long unLockTime = currentTime+LOCK_TIMEOUT;
        if (!vneLocks.containsKey(vneId)) {
            vneLocks.put(vneId, new ConcurrentHashMap<LocalDate, Long>());
            vneLocks.get(vneId).put(date, unLockTime);
            return true;
        }else {
            if(currentTime > vneLocks.get(vneId).get(date)) {
                ConcurrentHashMap<LocalDate,Long> lockedDates = vneLocks.get(vneId);
                lockedDates.putIfAbsent(date, unLockTime);
                return true;
            }else {return false;}
        }
    }

    public void unlockDate(Integer vneId, LocalDate date) {
        vneLocks.get(vneId).remove(date);
    }

    public boolean islockDate(Integer vneId, LocalDate date) {
        Long currentTime = System.currentTimeMillis();
        if(!vneLocks.get(vneId).containsKey(date)) {
            return true;
        }else {
            if (currentTime < vneLocks.get(vneId).get(date)) {
                return true;
            }else {return false;}
        }
    }

}
