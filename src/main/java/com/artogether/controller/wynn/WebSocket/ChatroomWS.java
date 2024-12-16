package com.artogether.controller.wynn.WebSocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

@ServerEndpoint("/ChatroomWS/{role}/{id}")
public class ChatroomWS {
	private static Map<String, Session> sessionsMap = new ConcurrentHashMap<>();

	@Autowired
	private Gson gson;
	
	@OnOpen
    public void onOpen(@PathParam("role") String role, @PathParam("id") String id, Session userSession) {
        String userKey = role + "-" + id;
        sessionsMap.put(userKey, userSession);
        System.out.println("Connected: " + userKey);
    }
	
//	 @OnMessage
//	    public void onMessage(Session userSession, String message) {
//	        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
//	        String receiverKey = chatMessage.getReceiverRole() + "-" + chatMessage.getReceiverId();
//
//	        Session receiverSession = sessionsMap.get(receiverKey);
//	        if (receiverSession != null && receiverSession.isOpen()) {
//	            receiverSession.getAsyncRemote().sendText(message);
//	        } else {
//	            // 保存離線訊息到資料庫或其他存儲
//	            saveOfflineMessage(chatMessage);
//	        }
//	    }
//	
	@OnError
	public void onError() {
		
	}
	
	@OnClose
	public void onClose() {}
}
