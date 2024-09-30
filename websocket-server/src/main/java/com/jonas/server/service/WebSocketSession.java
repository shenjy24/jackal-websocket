package com.jonas.server.service;

import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket 用户会话信息
 *
 * @author shenjy
 * @version 1.0
 * @date 2024-09-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketSession {
    private Long userId;
    private Session session;
}
