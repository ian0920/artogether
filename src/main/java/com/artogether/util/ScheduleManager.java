package com.artogether.util;

import com.artogether.event.event.Event;
import com.artogether.event.event.EventRepo;
import com.artogether.product.product.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleManager {

    @Autowired
    EventRepo eventRepo;
    @Autowired
    private ProductServiceImpl productServiceImpl;


    /*
        活動狀態變更
             商家 -> 報名中or延期 ->活動結束
             會員 -> 訂單活動狀態變更
             會員 -> 取消報名按鈕變更
             會員 -> 取消報名功能變更 (延期的狀況考慮)
     */

    //每天 00:00執行 延遲10秒開始
    @Scheduled(cron =" 0 0 0 * * *", zone ="Asia/Taipei")
    public void eventStatusUpdate(){
        //撈出狀態為1 報名中 2 延期 的活動
        List<Byte> status = new ArrayList<>();
        status.add((byte) 1);
        status.add((byte) 2);

        List<Event> eventList = eventRepo.findAllByStatusIsIn(status);


        //將報名中的活動開始時間 > 現在時間的活動 更改狀態為結束報名
        eventList.stream()
                .filter(e -> e.getStatus() == (byte) 1)
                .filter(e -> e.getStartDate().after(new Timestamp(System.currentTimeMillis())))
                .forEach(e -> e.setStatus((byte) 5));

        //將延期的活動 延期的時間 > 現在的時間 更改活動狀態為結束報名
        eventList.stream()
                .filter(e -> e.getStatus() == (byte) 2)
                .filter(e -> e.getDelayDate().after(new Timestamp(System.currentTimeMillis())))
                .forEach(e -> e.setStatus((byte) 5));
    }

    /*
        優惠券狀態變更
            商家 - end date < 現在日期 -> 狀態更改 (已過期)
            會員 - 收到日期 + 效期 > 現在日期 -> 狀態更改 (已過期)
            會員 - 活動日期 > 現在日期 -> 狀態更改 (已過期)


     */


    /*

        商品 -> 商家 -> 優惠券 -> 我的優惠券

     */





}
