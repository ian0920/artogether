package com.artogether.venue.tslot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TslotServiceImpl {

    @Autowired
    private TslotRepository tslotRepository;

    // 創建時段
    public Tslot createTslot(Tslot tslot) {
        return tslotRepository.save(tslot);
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

    // 更新時段
    public Tslot updateTslot(int id, Tslot tslot) {
        if (tslotRepository.existsById(id)) {
            tslot.setId(id); // 確保ID不變
            return tslotRepository.save(tslot);
        }
        return null;
    }

    // 刪除時段
    public void deleteTslot(int id) {
        if (tslotRepository.existsById(id)) {
            tslotRepository.deleteById(id);
        }
    }
}
