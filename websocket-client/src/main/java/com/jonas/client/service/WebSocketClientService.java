package com.jonas.client.service;

import com.jonas.client.config.MyWebSocketClient;
import com.jonas.client.proto.HelloProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.stereotype.Service;

/**
 * WebSocketClientService
 *
 * @author shenjy
 * @version 1.0
 * @date 2024-09-30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketClientService {

    private final WebSocketClient webSocketClient;

    public void sendMessage() {
        // 创建Protobuf消息
        HelloProto.Hello message = HelloProto.Hello.newBuilder()
                .setId(12)
                .setCode("2000")
                .setName("Tom")
                .build();
        webSocketClient.send(message.toByteArray());
    }
}
