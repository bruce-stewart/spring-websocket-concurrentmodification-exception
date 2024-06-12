package com.example.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@Slf4j
public class SubscriptionEventListener implements ApplicationListener<SessionSubscribeEvent> {
    @Override
    public void onApplicationEvent(@NonNull SessionSubscribeEvent event) {
        log.info("\nSessionSubscribeEvent: {}", event);
        SimpMessageHeaderAccessor wrapped = SimpMessageHeaderAccessor.wrap(event.getMessage());
        log.info("\nSimpMessageHeaderAccessor: {}", wrapped);
    }
}
