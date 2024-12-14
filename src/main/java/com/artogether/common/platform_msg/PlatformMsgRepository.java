package com.artogether.common.platform_msg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformMsgRepository extends JpaRepository<PlatformMsg, Integer> {


    List<PlatformMsg> findByBusinessMember_Id(Integer businessId);

    List<PlatformMsg> findByMember_Id(Integer memberId);
}
