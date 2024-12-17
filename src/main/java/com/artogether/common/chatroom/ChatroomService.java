package com.artogether.common.chatroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.common.member.Member;
import com.artogether.common.member.MemberService;

@Service
public class ChatroomService {
	
	@Autowired
	private ChatroomRepo repo;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private MemberService memberService;
	
	public List<Chatroom> getChatroomsByMemberId(Integer memberId) {
		return repo.findAllByMember_Id(memberId);
	}
	
	public List<Chatroom> getChatroomsByBMemberId(Integer bmembId) {
		return repo.findAllByBusinessMemberId(bmembId);
	}
	
//	public void create(Integer membId, Integer bmembId) {
//		Chatroom chatroom = Chatroom.builder().build();
//		repo.save(chatroom);
//	}
	
	public void create(Member member, BusinessMember bMember) {
		Chatroom chatroom = Chatroom.builder().member(member).businessMember(bMember).build();
		repo.save(chatroom);
	}
	
	public Chatroom findOrCreateChatroom(Integer memberId, Integer businessMemberId) {
	    Chatroom chatroom = repo.findByMemberIdAndBusinessMemberId(memberId, businessMemberId);
	    if (chatroom == null) {
	    	BusinessMember b = businessService.findById(businessMemberId);
	    	Member m = memberService.findById(memberId);
	        chatroom = Chatroom.builder().businessMember(b).member(m).build();
	        repo.save(chatroom);
	    }
	    return chatroom;
	}

}
