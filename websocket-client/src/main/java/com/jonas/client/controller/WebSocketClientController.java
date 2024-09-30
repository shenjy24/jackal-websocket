package com.jonas.client.controller;

import com.jonas.client.service.WebSocketClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WebSocketClientController
 *
 * @author shenjy
 * @version 1.0
 * @date 2024-09-30
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ws/client")
public class WebSocketClientController {

    private final WebSocketClientService webSocketClientService;

    @PostMapping("/sendMessage")
    public void sendMessage() {
        webSocketClientService.sendMessage();
    }
}
