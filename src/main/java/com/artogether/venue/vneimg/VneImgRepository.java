package com.artogether.venue.vneimg;

import com.artogether.venue.venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VneImgRepository extends JpaRepository<VneImg, Integer> {
    List<VneImg> findAllByVneId(Venue vneId);
    VneImg findByVneId(Venue vneId);

}
