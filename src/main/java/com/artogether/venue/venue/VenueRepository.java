package com.artogether.venue.venue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {
    List<Venue> findByBusinessMember_Id(int bizId);
    Boolean existsByIdAndStatus(int vneId, VenueStatusEnum status);
    List<Venue> findByStatus(VenueStatusEnum status);


}
