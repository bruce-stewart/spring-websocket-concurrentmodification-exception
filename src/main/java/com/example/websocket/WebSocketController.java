package com.example.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

/**
 * @author bruce.stewart@altron.com
 */
@Controller
@Slf4j
public class WebSocketController {

    @SubscribeMapping({"/ping"})
    public String handlePing(SimpMessageHeaderAccessor headerAccessor) {
        log.info("\nHeader: {}", headerAccessor.toString());
        return "pong";
    }

}
