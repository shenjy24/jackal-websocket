package com.jonas.client.config;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebSocketClientConfig
 *
 * @author shenjy
 * @version 1.0
 * @date 2024-09-30
 */
@Slf4j
@Configuration
public class WebSocketClientConfig {

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            String wsServerUrl = "ws://127.0.0.1:8080/ws/1";
            MyWebSocketClient webSocketClient =
                    new MyWebSocketClient(new URI(wsServerUrl));
            webSocketClient.connect();
            return webSocketClient;
        } catch (URISyntaxException e) {
            log.error("ws connect error", e);
        }
        return null;
    }
}
