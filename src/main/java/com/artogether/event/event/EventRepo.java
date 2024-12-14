package com.artogether.event.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {

    List<Event> findByBusinessMember_Id(Integer businessId);

    List<Event> findAllByStatusIsIn(List<Byte> status);
}
