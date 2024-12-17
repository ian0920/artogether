package com.artogether.controller.wynn;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.common.business_perm.BusinessPerm;
import com.artogether.common.business_perm.BusinessPerm.BusinessPermComposite;
import com.artogether.common.business_perm.BusinessPermService;
import com.artogether.common.member.Member;
import com.artogether.common.member.MemberService;
import com.artogether.common.message.Message;
import com.artogether.common.message.MessageService;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_img.EvtImgService;
import com.artogether.event.evt_track.EvtTrackService;
import com.artogether.util.MailManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/api") 
public class Wynn_RestController {
	@Autowired
	private EventService eventService;
	
	@Autowired
	private EvtTrackService evtTrackService;

	@Autowired
	private EvtImgService evtImgService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private BusinessPermService businessPermService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MailManager mailManager;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
    private Jedis jedis;


	//==========================================================
	//         				活動相關
	//==========================================================
	
	// 追蹤、取消追蹤活動
	@PostMapping("/event/toggleTrack")
	public ResponseEntity<Map> trackEvent(@RequestBody Map<String, Object> payload) {
        Integer evtId = (Integer) payload.get("eventId");
        Integer memberId = (Integer) payload.get("memberId");
        // TODO: 會員ONLY
		
		String msg=evtTrackService.toggleEvtTrack(memberId,evtId);
		Map<String, String> response = new HashMap<>();
        response.put("message", msg);
        return ResponseEntity.ok(response);
	}
	
	// 刪除活動圖片
	@PostMapping("/event/deleteEvtImg")
	public ResponseEntity<Map> deleteEvtImg(@RequestBody Map<String, Object> payload){
		List<Integer> imgList = (List<Integer>) payload.get("evtImgId");
		System.out.println(imgList);
		evtImgService.deleteByIdList(imgList);
		Map<String, String> response = new HashMap<>();
        response.put("message", "成功刪除");
        return ResponseEntity.ok(response);
		
	}
	
	// (商家)更新活動狀態
	@PostMapping("/event/updateStatus")
	public ResponseEntity<Map> updateStatus(@RequestBody Map<String, Object> payload){
		Integer evtId = (Integer) payload.get("eventId");
        Byte status = ((Integer)payload.get("status")).byteValue();
        Timestamp delayDate = null;
        // 確認 payload 中是否有延期日期
        if (payload.get("delayDate") != null) {
            String delayDateString = (String) payload.get("delayDate");
            try {
                // 使用 ISO_DATE_TIME 解析日期時間字串
                LocalDateTime localDateTime = LocalDateTime.parse(delayDateString, DateTimeFormatter.ISO_DATE_TIME);
                delayDate = Timestamp.valueOf(localDateTime);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("message", "無效的日期格式"));
            }
        }

        Event e=  Event.builder().id(evtId).status(status).delayDate(delayDate).build();
		eventService.statusUpdate(e);
		Map<String, String> response = new HashMap<>();
        response.put("message", "成功更新狀態");
        return ResponseEntity.ok(response);
		
	}
	
	//==========================================================
	//         				商家相關
	//==========================================================
	
	//商家新增員工
	@PostMapping("/business/addStaff")
	public ResponseEntity<Map> addStaff(@RequestBody Map<String, Object> payload, HttpSession session){
		List<String> emails = (List) payload.get("emails");
		Map<String, Boolean> perms = (Map) payload.get("perms");
		BusinessMember bm = (BusinessMember)session.getAttribute("presentBusinessMember");
		List<BusinessPerm> bPerms = new ArrayList<>();
		List<String> nonMemberList=new ArrayList<>();
		System.out.println(payload);
		for (String email : emails) {
			Member m =memberService.findByEmail(email);
			if(m==null) {
				nonMemberList.add(email);
			}else {
			// 新增權限
	        BusinessPerm bp = new BusinessPerm();
	        bp.setBusinessMember(bm);
	        bp.setMember(m);
	        bp.setPrdPerm(perms.getOrDefault("prdPerm", false));
	        bp.setVnePerm(perms.getOrDefault("vnePerm", false));
	        bp.setEvtPerm(perms.getOrDefault("evtPerm", false));
	        bp.setAdminPerm(false);
	        bp.setStatus(Byte.valueOf((byte) 0));
	        bPerms.add(bp);
			}
		}
		
		List<BusinessPerm> doneBPerms=businessPermService.saveAll(bPerms);
		// 整理響應訊息
	    String addedEmails = doneBPerms.stream()
	            .map(bp -> bp.getMember().getEmail())
	            .collect(Collectors.joining(", "));
	    String nonMemberEmails = String.join(", ", nonMemberList);

	    String message = "成功新增以下員工: " + addedEmails;
	    if (!nonMemberList.isEmpty()) {
	        message += "；以下人員未註冊，已發送邀請郵件: " + nonMemberEmails;
	        // 發送邀請邏輯（假設有 inviteService）
	        nonMemberList.forEach(email ->mailManager.sendInviteMail(email, bm.getName()));
	    }

	    return ResponseEntity.ok(Map.of("message", message, "newStaff", doneBPerms));
		
	}
	
	//==========================================================
	//         				平台相關
	//==========================================================
	
	// 更新商家會員狀態(審核、停權、啟用)
	@PostMapping("/platform/updateBMemberStatus")
	public ResponseEntity<Map> updateBMemberStatus(@RequestBody Map<String, Object> payload){
		Integer bMembId = (Integer) payload.get("bMembId");
		Byte status = ((Integer)payload.get("status")).byteValue();
		BusinessMember b = BusinessMember.builder().id(bMembId).status(status).build();
		// 如果是第一次啟用則要修改approveDate
		if(businessService.findById(bMembId).getApproveDate()==null) {
			b.setApproveDate(new Timestamp(System.currentTimeMillis()));
		}
		businessService.statusUpdate(b);
		Map<String, String> response = new HashMap<>();
		response.put("message", "成功更新狀態");
		return ResponseEntity.ok(response);
		
	}
	
	//=========================================================
	//                        聊天室
	//=========================================================
	// 取得歷史訊息
		@PostMapping("/chat/getHistory")
		public ResponseEntity<Map> getChatHistory(@RequestBody Map<String, Object> payload){
			Integer chatroomId = (Integer) payload.get("chatroomId");
			//TODO: 應該先取redis的資料?
	        String chatroomKey = "chatroom:" + chatroomId;
	        var chatroomMessages = jedis.lrange(chatroomKey, 0, -1);
	        // 將 Redis 的 JSON 字串轉換為 Java 的 Map 或物件
	        List<Map<String, Object>> parsedMessages = new ArrayList<>();
	        for (String msg : chatroomMessages) {
	            try {
	                Map<String, Object> messageMap = new ObjectMapper().readValue(msg, Map.class);
	                parsedMessages.add(messageMap);
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        }
	        // 反轉順序
	        Collections.reverse(parsedMessages);
	        
	        System.out.println(chatroomMessages);
//			List<Message> msgList = messageService.getChatHistory(chatroomId);
			
			Map<String, Object> response = new HashMap<>();
			response.put("msgList", parsedMessages);
			response.put("message", parsedMessages.isEmpty()?"查無歷史訊息":"成功取得歷史訊息");

			return ResponseEntity.ok(response);			
		}
}
