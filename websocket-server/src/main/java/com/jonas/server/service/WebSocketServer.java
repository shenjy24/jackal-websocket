package com.jonas.server.service;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/ws/{userId}")
public class WebSocketServer {

    /**
     * 用来存放每个客户端对应的 WebSocketServer 对象
     */
    private static final ConcurrentHashMap<Long, WebSocketSession> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        if (webSocketMap.containsKey(userId)) {
            WebSocketSession webSocketSession = webSocketMap.remove(userId);
            try {
                webSocketSession.getSession().close();
            } catch (IOException e) {
                log.error("已有的WS连接关闭异常", e);
            }
            webSocketMap.put(userId, new WebSocketSession(userId, session));
        } else {
            webSocketMap.put(userId, new WebSocketSession(userId, session));
        }
        log.info("用户 {} WS连接成功", userId);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        if (session == null) {
            return;
        }
        WebSocketSession webSocketSession = getWebSocketBySession(session);
        if (webSocketSession == null) {
            return;
        }

        webSocketMap.values().removeIf(e -> session.getId().equals(e.getSession().getId()));
        log.info("用户 {} 断开连接", webSocketSession.getUserId());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        WebSocketSession webSocketSession = getWebSocketBySession(session);
        if (webSocketSession != null) {
            log.info("用户消息:{}, 报文:{}", webSocketSession.getUserId(), message);
            sendMessage(webSocketSession.getUserId(), message);
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session 会话
     * @param error   错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        WebSocketSession webSocketSession = getWebSocketBySession(session);
        if (webSocketSession != null) {
            log.error("用户错误:{}, 原因:{}", webSocketSession.getUserId(), error.getMessage());
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Long userId, String message) {
        WebSocketSession webSocketSession = webSocketMap.get(userId);
        if (webSocketSession == null) {
            log.error("用户 {} WS连接不存在", userId);
            return;
        }
        try {
            webSocketSession.getSession().getBasicRemote().sendText(message);
            log.info("用户 {} WS发送消息 {}", userId, message);
        } catch (IOException e) {
            log.error("WS发送消息异常, userId:{}, message:{}, error:{}", userId, message, e.getMessage());
        }
    }

    private WebSocketSession getWebSocketBySession(Session session) {
        if (session == null) {
            return null;
        }
        String sessionId = session.getId();
        for (Map.Entry<Long, WebSocketSession> entry : webSocketMap.entrySet()) {
            if (entry.getValue().getSession().getId().equals(sessionId)) {
                return entry.getValue();
            }
        }
        return null;
    }
}