package com.example.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

/**
 * @author bruce.stewart@altron.com
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    public static final String APP_DEST_PREFIX = "/app";
    public static final String USER_DESTINATION_PREFIX = "/user";
    public static final String ENDPOINT = "/example";

    public void configureMessageBroker(MessageBrokerRegistry registry) {
        int serverHeartbeatSendInterval = 0;
        int clientWriteInterval = 0;
        registry.enableSimpleBroker("/queue", "/topic")
                .setHeartbeatValue(new long[] {serverHeartbeatSendInterval, clientWriteInterval});
        registry.setUserDestinationPrefix(USER_DESTINATION_PREFIX);
        registry.setApplicationDestinationPrefixes(APP_DEST_PREFIX);
        /*
         https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-stomp-ordered-messages
         */
        registry.setPreservePublishOrder(true);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setPreserveReceiveOrder(true);
        registry.addEndpoint(ENDPOINT)
                .setAllowedOriginPatterns("http://*:*");
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(new MappingJackson2MessageConverter());
        return false;
    }

}
