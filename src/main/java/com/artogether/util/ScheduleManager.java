package com.artogether.util;

import com.artogether.event.event.Event;
import com.artogether.event.event.EventRepo;
import com.artogether.event.evt_coup.EvtCoup;
import com.artogether.event.evt_coup.EvtCoupRepo;
import com.artogether.event.my_evt_coup.MyEvtCoup;
import com.artogether.event.my_evt_coup.MyEvtCoupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
@Transactional
public class ScheduleManager {

    @Autowired
    EventRepo eventRepo;
    @Autowired
    private EvtCoupRepo evtCoupRepo;
    @Autowired
    private MyEvtCoupRepo myEvtCoupRepo;


    /*
        活動狀態變更
             商家 -> 報名中or延期 -> 活動結束
             會員 -> 訂單活動狀態變更
             會員 -> 取消報名按鈕變更
             會員 -> 取消報名功能變更 (延期的狀況考慮)
     */

    //每天 00:00:10執行
    @Scheduled(cron =" 10 30 10 * * *", zone ="Asia/Taipei")
    public void eventStatusUpdate(){
        System.out.println("活動狀態更新排程器啟動");
        //撈出狀態為1 報名中 2 延期 的活動
        List<Byte> status = new ArrayList<>();
        status.add((byte) 1);
        status.add((byte) 2);

        List<Event> eventList = eventRepo.findAllByStatusIsIn(status);


        //將報名中的活動開始時間 > 現在時間的活動 更改狀態為結束報名
        eventList.stream()
                .filter(e -> e.getStatus() == (byte) 1)
                .filter(e -> e.getStartDate().before(new Timestamp(System.currentTimeMillis())))
                .forEach(e -> e.setStatus((byte) 5));

        //將延期的活動 延期的時間 > 現在的時間 更改活動狀態為結束報名
        eventList.stream()
                .filter(e -> e.getStatus() == (byte) 2)
                .filter(e -> e.getDelayDate().before(new Timestamp(System.currentTimeMillis())))
                .forEach(e -> e.setStatus((byte) 5));

        eventRepo.saveAll(eventList);

        System.out.println("活動狀態更新排程器完成");
    }



    /*      優惠券狀態變更     */

    /*   商家 - end date < 現在日期 -> 狀態更改 (過期)
         會員 - 過期優惠券 -> 狀態更改 (失效)            */
    //每天 00:00:10執行
    @Scheduled(cron =" 10 30 10 * * *", zone ="Asia/Taipei")
    public void evtCoupStatusUpdate(){

        try{

            System.out.println("活動優惠券狀態更新排程器啟動");

            List<Integer> changedEvtCoupId = new ArrayList<>();
            List<EvtCoup> allEvtCoup = evtCoupRepo.findAllByStatus((byte) 1);
            //確認上架的優惠券是否在期限內，超過期限更改狀態為過期(2)
            allEvtCoup.stream().filter(e -> e.getEndDate().before(new Timestamp(System.currentTimeMillis())))
                    .forEach(e ->
                    {e.setStatus((byte) 2);
                        changedEvtCoupId.add(e.getId());});

            //將剛修改為過期的優惠券，同步更新狀態至我的優惠券為失效
            List<MyEvtCoup> allMyEvtCoup = myEvtCoupRepo.findAllByEvtCoup_IdIn(changedEvtCoupId);
            allMyEvtCoup.forEach(e -> e.setStatus((byte) 2));

            evtCoupRepo.saveAll(allEvtCoup);
            myEvtCoupRepo.saveAll(allMyEvtCoup);

            System.out.println("活動優惠券狀態更新排程器完成");

        } catch (RuntimeException e) {

            e.printStackTrace();
            System.err.println("活動優惠券狀態更新排程器發生錯誤");

        }


    }

    /*   會員 - 收到日期 + 效期 > 現在日期 -> 狀態更改 (失效)   */
    //每天 00:00:10執行
    @Scheduled(cron =" 10 30 10 * * *", zone ="Asia/Taipei")
    public void myEvtCoupStatusUpdate(){

        try{
            System.out.println("我的活動優惠券狀態更新排程器啟動");

            List<MyEvtCoup> allMyEvtCoup = myEvtCoupRepo.findAllByStatus((byte) 0);

            //撈出未使用的我的優惠券 -> 目前狀態是上架中的優惠券 -> 確認是否在效期內(收到日 + 效期 > 今天 -> 失效)
            //                                                -> 效期 == null -> 活動開始日 > 今天 -> 失效

            Predicate<MyEvtCoup> condition = e -> {

                Long today = new Timestamp(System.currentTimeMillis()).getTime();

                    if(e.getEvtCoup().getDuration() != null){
                        if (e.getReceiveDate().getTime()+(e.getEvtCoup().getDuration()*86400000) > today ){
                            return true;
                        }
                    } else {
                        if(e.getEvtCoup().getStartDate().before(new Timestamp(System.currentTimeMillis())))
                            return true;
                    }
                    return false;
            };

            allMyEvtCoup.stream()
                    .filter(e -> e.getEvtCoup().getStatus() == (byte) 1)
                    .filter(condition)
                    .forEach(e -> e.setStatus((byte) 2));

            myEvtCoupRepo.saveAll(allMyEvtCoup);

            System.out.println("我的活動優惠券狀態更新排程器完成");

        }catch (RuntimeException e){

            e.printStackTrace();
            System.err.println("我的活動優惠券狀態更新排程器發生錯誤");
        }

    }

    /*   會員 - 活動日期 > 現在日期 -> 狀態更改 (失效)   */
    //每天 00:00:10執行
//    @Scheduled(cron =" 10 0 0 * * *", zone ="Asia/Taipei")
//    public void myEvtCoupStatusUpdate2(){
//        System.out.println("我的活動優惠券狀態更新排程器2啟動");
//
//        List<MyEvtCoup> allMyEvtCoup = myEvtCoupRepo.findAllByStatus((byte) 0);
//
//        allMyEvtCoup.stream()
//                .filter(e -> e.getEvtCoup().getStartDate().before(new Timestamp(System.currentTimeMillis())))
//                .forEach(e -> e.setStatus((byte) 2));
//
//        myEvtCoupRepo.saveAll(allMyEvtCoup);
//
//        System.out.println("我的活動優惠券狀態更新排程器2完成");
//    }




}
