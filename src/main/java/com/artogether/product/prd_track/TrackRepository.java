package com.artogether.product.prd_track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrackRepository extends JpaRepository<Prd_Track, Prd_Track.PrdTrackId> {
}
