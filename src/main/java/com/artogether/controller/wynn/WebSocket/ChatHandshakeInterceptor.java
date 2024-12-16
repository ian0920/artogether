package com.artogether.controller.wynn.WebSocket;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.artogether.common.business_member.BusinessMember;

@Component
public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        // 獲取 HttpSession（如果存在）
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpSession httpSession = servletRequest.getServletRequest().getSession(false);
            if (httpSession != null) {
                // 將 Integer 屬性複製到 WebSocketSession 屬性
                Integer integerValue = (Integer) httpSession.getAttribute("member");
                if (integerValue != null) {
                    attributes.put("memberId", integerValue);
                }

                // 將 BusinessMember 屬性複製到 WebSocketSession 屬性
                BusinessMember businessMember = (BusinessMember) httpSession.getAttribute("presentBusinessMember");
                if (businessMember != null) {
                    attributes.put("businessMember", businessMember);
                }
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}