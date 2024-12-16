package com.artogether.common.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	@Autowired
	private MessageRepo repo;
	
	public List<Message> getChatHistory(Integer chatroomId) {
	    return repo.findByChatroom_IdOrderBySendTimeAsc(chatroomId);
	}
	
	public void saveAll(List<Message> msgList) {
		repo.saveAll(msgList);
	}
}
