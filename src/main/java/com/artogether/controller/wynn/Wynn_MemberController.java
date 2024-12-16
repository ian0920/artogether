package com.artogether.controller.wynn;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.artogether.common.chatroom.Chatroom;
import com.artogether.common.chatroom.ChatroomService;

@Controller
public class Wynn_MemberController {
	
	@Autowired
	private ChatroomService chatroomService;
	
	@GetMapping("/member/chatroom")
	public String goChatroom(Model model, HttpSession session) {
		Integer membId = (Integer)session.getAttribute("member");
		List<Chatroom> chatrooms = chatroomService.getChatroomsByMemberId(membId);
		model.addAttribute("chatrooms", chatrooms);
		return "frontend/chatroom_member";
	}
	
	@GetMapping("/member/sendMessage")
	public String sendMessage(@RequestParam Integer bmId, @RequestParam Integer mId) {
		chatroomService.findOrCreateChatroom(mId, bmId);
		return "redirect:/member/chatroom";
	}
}
