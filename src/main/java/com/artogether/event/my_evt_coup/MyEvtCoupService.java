package com.artogether.event.my_evt_coup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
