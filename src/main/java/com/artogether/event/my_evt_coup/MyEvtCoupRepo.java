package com.artogether.event.my_evt_coup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MyEvtCoupRepo extends JpaRepository<MyEvtCoup, MyEvtCoup.Composite> {
    @Query("select mec from MyEvtCoup mec where mec.member.id = :memberId and mec.evtCoup.event.id = :eventId")
    List<MyEvtCoup> getMyEvtCoupsByMemberIdAndEventId(Integer memberId, Integer eventId);

    List<MyEvtCoup> findAllByMember_Id(Integer memberId);

    List<MyEvtCoup> findAllByStatus(Byte status);

    List<MyEvtCoup> findAllByEvtCoup_IdIn(List<Integer> evtCoupIds);

    List<MyEvtCoup> findAllByEvtCoup_Id(Integer evtCoupId);
}
