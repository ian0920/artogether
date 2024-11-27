package com.artogether.venue.vneimg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VneImgRepository extends JpaRepository<VneImg, Integer> {
    List<VneImg> findAllByVenueId(Integer vneId);
    Optional<VneImg> findByVenueIdAndPosition(Integer vneId, Integer position);

}
