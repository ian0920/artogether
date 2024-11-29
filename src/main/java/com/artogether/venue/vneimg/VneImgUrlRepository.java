package com.artogether.venue.vneimg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VneImgUrlRepository extends JpaRepository<VneImgUrl, Integer> {
    @Query(value = "SELECT image_file_url FROM vne_img_url WHERE vne_id = ?1 ORDER BY position ", nativeQuery = true)
    List<String> findImageUrlsByVneId(Integer vneId);
    List<VneImgUrl> findAllByVenueId(Integer vneId);
    Optional<VneImgUrl> findByVenueIdAndPosition(Integer vneId, Integer position);
    Boolean existsByVenueIdAndPosition(Integer vneId, Integer position);
}
