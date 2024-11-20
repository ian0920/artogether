package com.artogether.event.my_evt_coup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyEvtCoupRepo extends JpaRepository<MyEvtCoup, MyEvtCoup.Composite> {
}
