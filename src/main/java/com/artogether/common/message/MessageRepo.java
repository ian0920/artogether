package com.artogether.common.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message, Integer>{


	List<Message> findByChatroom_IdOrderBySendTimeAsc(Integer chatroomId);

}
