package com.artogether.product.prd_track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PrdTrackRepository extends JpaRepository<PrdTrack, PrdTrack.PrdTrackId> {
}
