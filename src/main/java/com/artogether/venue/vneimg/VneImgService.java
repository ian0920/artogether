package com.artogether.venue.vneimg;

import com.artogether.venue.venue.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VneImgService {

    @Autowired
    private VneImgRepository vneImgRepository;

//    public List<VneImg> getAllImg(Integer vneId){
//        Venue venue = new Venue();
//        venue.setId(vneId);
//        return vneImgRepository.findAllByVneId(venue);
//    }


}
