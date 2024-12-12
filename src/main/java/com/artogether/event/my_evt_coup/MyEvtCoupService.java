package com.artogether.event.my_evt_coup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MyEvtCoupService {

    @Autowired
    private MyEvtCoupRepo repo;


    //null handling not be done
    public MyEvtCoup findById(MyEvtCoup myEvtCoup) {

        MyEvtCoup.Composite composite = new MyEvtCoup.Composite(myEvtCoup.getMember().getId(), myEvtCoup.getEvtCoup().getId());
        return repo.findById(composite).orElse(new MyEvtCoup());

    }

    public List<MyEvtCoup> findAll() {
        return repo.findAll();
    }

    public MyEvtCoup save(MyEvtCoup myEvtCoup) {
        return repo.save(myEvtCoup);
    }

    public void statusUpdate(MyEvtCoup myEvtCoup) {

        MyEvtCoup mec = findById(myEvtCoup);
        mec.setStatus(myEvtCoup.getStatus());
        repo.save(mec);

    }

    //回傳該會員特定活動的優惠券
    public List<MyEvtCoup> getMyEvtCoupsByMemberIdAndEventId(Integer memberId, Integer eventId) {
        return repo.getMyEvtCoupsByMemberIdAndEventId(memberId, eventId);

    }

    //我的優惠券頁面資訊
    public Map<String, List<MyEvtCoup>> findByMemberId(Integer memberId) {

        Map<String, List<MyEvtCoup>> map = new HashMap<>();

        List<MyEvtCoup> myEvtCoups = repo.findAllByMember_Id(memberId);

        for(MyEvtCoup evc : myEvtCoups){
            map.put(evc.getEvtCoup().getEvent().getName(), new ArrayList<MyEvtCoup>());

        }

        for(MyEvtCoup evc : myEvtCoups){
            Set<Map.Entry<String, List<MyEvtCoup>>> entrySet = map.entrySet();
            for (Map.Entry<String, List<MyEvtCoup>> entry : entrySet){
                if(entry.getKey().equals(evc.getEvtCoup().getEvent().getName())){
                    entry.getValue().add(evc);
                }
            }
        }


        return map;

    }
}
