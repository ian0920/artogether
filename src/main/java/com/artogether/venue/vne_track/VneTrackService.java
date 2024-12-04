package com.artogether.venue.vne_track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VneTrackService {

    @Autowired
    private VneTrackRepository vne_trackRepo;

    //新增場地追蹤
    public void add(VneTrack vne_track) {
        vne_trackRepo.save(vne_track);
    }

    //更新
    public void update(VneTrack vne_track) {
        vne_trackRepo.save(vne_track);
    }

    //刪除
    public void delete(VneTrack vne_track) {
        vne_trackRepo.delete(vne_track);
    }

    //查詢全部追蹤
    public List<VneTrack> getAll() {
        return vne_trackRepo.findAll();
    }

    //單一查詢
    public VneTrack getById(int id) {
        return vne_trackRepo.findById(id).get();
    }

}
