package com.artogether.controller.wynn.WebSocket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.chatroom.Chatroom;
import com.artogether.common.chatroom.ChatroomService;
import com.artogether.common.message.Message;
import com.artogether.common.message.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

@Component
public class ChatHandler extends TextWebSocketHandler {
	
	// 每次儲存到 MySQL 的最大消息數量
    private static final int MESSAGE_BATCH_THRESHOLD = 100;	
        
	@Autowired
    private Jedis jedis;
	
	@Autowired
	private ChatroomService chatroomService;
	
	@Autowired
	private MessageService messageService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 新增處理Timestamp的配置
    public ChatHandler() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }


    // 保存所有Client的WebSocket session instances:
    private Map<String, WebSocketSession> onlineUsers = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();

        // 儲存session
       saveUserSession(session);
        
//        // 推送離線消息（如果有）
//        String offlineKey = "offline_messages:" + getUserKeyfromAttributes(attributes);
//        var offlineMessages = jedis.lrange(offlineKey, 0, -1);
//        for (String jsonMessage : offlineMessages) {
//            session.sendMessage(new TextMessage(jsonMessage));
//        }
//        // 清空離線消息
//        jedis.del(offlineKey);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	System.out.println(message.getPayload());
    	// 處理發送的消息
        Message chatMessage = objectMapper.readValue(message.getPayload(), Message.class);
        System.out.println(chatMessage);
        
        // 根據聊天室 id 儲存消息
        Integer chatRoomId = chatMessage.getChatroom().getId();
        String redisChatKey = "chatroom:" + chatRoomId;

        // 儲存消息到 Redis（用於暫存）
        jedis.lpush(redisChatKey, objectMapper.writeValueAsString(chatMessage));
        
        // 檢查 Redis 中消息數量，並在達到閾值時儲存到 MySQL
        long messageCount = jedis.llen(redisChatKey);
        if (messageCount >= MESSAGE_BATCH_THRESHOLD) {
            // 批量儲存到 MySQL
            saveMessagesToDatabase(redisChatKey);

            // 清空 Redis 中的暫存消息
            jedis.del(redisChatKey);
        }
        
        String receiverKey = getUserKeyfromMessage(false, chatMessage);
        String senderKey = getUserKeyfromMessage(true, chatMessage);
        
        // 儲存至 WebSocket session 發送
         WebSocketSession receiverSession = onlineUsers.get(receiverKey);
         WebSocketSession senderSession = onlineUsers.get(senderKey);
            // 如果接收者在線，立即發送訊息
            if (receiverSession != null && receiverSession.isOpen()) {
                receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            } else {
            // 如果接收者離線，先不發送，後續在離線消息中發送
//            	String offlineKey = "offline_messages:" +chatRoomId+ ":" +receiverKey;
//            	jedis.lpush(offlineKey, objectMapper.writeValueAsString(chatMessage));
            }
            
            // 如果發送者在線，立即發送訊息
            if (senderSession != null && senderSession.isOpen()) {
            	senderSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            } else {
            	// 如果寄送者離線，先不發送，後續在離線消息中發送
//            	String offlineKey = "offline_messages:" +chatRoomId+ ":" +senderKey;
//            	jedis.lpush(offlineKey, objectMapper.writeValueAsString(chatMessage));
            }
    }

	@Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 連接關閉時，移除用戶在線記錄
        Map<String, Object> attributes = session.getAttributes();
    	onlineUsers.remove(getUserKeyfromAttributes(attributes));
    }
	
	//=======================================================================

	private String getUserKeyfromAttributes(Map<String, Object> attributes) {
		String userKey;
        // 根據屬性判斷是 BusinessMember 還是普通用戶
        if (attributes.containsKey("presentBusinessMember")) {
            BusinessMember businessMember = (BusinessMember) attributes.get("presentBusinessMember");
            userKey = "bm_" + businessMember.getId();
        } else {
            Integer memberId = (Integer) attributes.get("memberId");
            userKey = "m_" + memberId;
        }
        return userKey;
	}
	
	private String getUserKeyfromMessage(Boolean wantSender, Message m) {
		if ((m.getSender()&&wantSender)||(!m.getSender())&&(!wantSender)) {
			//訊息為商家發送時，求寄送者key；或是訊息為會員發送且求接收者時-->求商家key
			return "bm_"+m.getChatroom().getBusinessMember().getId();
		}else {
			return "m_"+m.getChatroom().getMember().getId();
		}
	}
    
    private void saveUserSession(WebSocketSession session) {
    	Map<String, Object> attributes = session.getAttributes();
        String userKey = null;

        // 根據屬性判斷是 BusinessMember 還是普通用戶，並儲存session至map
        if (attributes.containsKey("presentBusinessMember")) {
            BusinessMember businessMember = (BusinessMember) attributes.get("presentBusinessMember");
            userKey = "bm_"+businessMember.getId();
            onlineUsers.put(userKey, session);
        } else if (attributes.containsKey("memberId")) {
            Integer memberId = (Integer) attributes.get("memberId");
            userKey = "m_"+memberId;
            onlineUsers.put(userKey, session);
        }
	}
    
    private void saveMessagesToDatabase(String redisChatKey) {
        // 從 Redis 讀取所有消息
        long messageCount = jedis.llen(redisChatKey);
        if (messageCount == 0) {
            return; // 沒有消息時，直接返回
        }

        // 批量處理消息
        List<Message> messages = new ArrayList<>();
        for (long i = 0; i < messageCount; i++) {
            String messageJson = jedis.rpop(redisChatKey); // 使用 rpop 移除並獲取消息
            try {
                // 將 JSON 字串轉換為 Message 實體
                Message message = objectMapper.readValue(messageJson, Message.class);
                messages.add(message);
            } catch (Exception e) {
                e.printStackTrace(); // 捕捉並列印轉換異常
            }
        }

        // 儲存到 MySQL
        if (!messages.isEmpty()) {
            try {
                messageService.saveAll(messages); // 使用批量儲存方法（需在 Service 中實現）
            } catch (Exception e) {
                e.printStackTrace(); // 捕捉並列印資料庫儲存異常
            }
        }
    }

}
