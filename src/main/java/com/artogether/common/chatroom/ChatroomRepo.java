package com.artogether.common.chatroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepo extends JpaRepository<Chatroom, Integer>{

	List<Chatroom> findAllByMember_Id(Integer memberId);

	List<Chatroom> findAllByBusinessMemberId(Integer bmembId);

	Chatroom findByMemberIdAndBusinessMemberId(Integer memberId, Integer businessMemberId);

}
