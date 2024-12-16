package com.artogether.listener;

import com.artogether.event.evt_coup.EvtCoup;
import com.artogether.event.evt_coup.EvtCoupRepo;
import com.artogether.event.my_evt_coup.MyEvtCoup;
import com.artogether.event.my_evt_coup.MyEvtCoupRepo;
import com.artogether.util.EvtCoupStatusChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class EvtCoupStatusChangeListener {


    private final EvtCoupRepo evtCoupRepo;

    private final MyEvtCoupRepo myEvtCoupRepo;

    public EvtCoupStatusChangeListener(EvtCoupRepo evtCoupRepo, MyEvtCoupRepo myEvtCoupRepo) {

        this.evtCoupRepo = evtCoupRepo;
        this.myEvtCoupRepo = myEvtCoupRepo;
    }


    @EventListener
    @Transactional
    public void handleEvtCoupStatusChange(EvtCoupStatusChangeEvent evtCoup) {

        EvtCoup ec = evtCoupRepo.findById(evtCoup.getEvtCoupId())
                .orElseThrow(() -> new RuntimeException("找不到活動優惠券"));

        //優惠券狀態更新為下架 -> 我的優惠券 若尚未使用則失效
        if (ec.getStatus() == (byte) 0){
            List<MyEvtCoup> allEvtCoup = myEvtCoupRepo.findAllByEvtCoup_Id(ec.getId());
            allEvtCoup.stream().filter(e -> e.getStatus() == (byte) 0)
                    .forEach(e -> {
                        e.setStatus((byte) 2);
                        myEvtCoupRepo.save(e);
                    });
        } else if (ec.getStatus() == (byte) 1) {
            //優惠券狀態更新為上架 -> 我的優惠券狀態若已使用 則不改為未使用
            List<MyEvtCoup> allEvtCoup = myEvtCoupRepo.findAllByEvtCoup_Id(ec.getId());
            allEvtCoup.stream().filter(e -> e.getStatus() != (byte) 1)
                    .forEach(e -> {
                        e.setStatus((byte) 0);
                        myEvtCoupRepo.save(e);
                    });

        }
    }


}
