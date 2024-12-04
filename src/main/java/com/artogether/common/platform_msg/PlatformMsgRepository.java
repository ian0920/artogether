package com.artogether.common.platform_msg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformMsgRepository extends JpaRepository<PlatformMsg, Integer> {

}
