package com.example.websocket;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author bruce.stewart@altron.com
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class WebSocketControllerTest {

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient webSocketStompClient;

    private StompSession session;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        this.webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
        this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        String url = String.format("ws://%s:%d/%s", "localhost", port, WebSocketConfiguration.ENDPOINT);
        this.session = webSocketStompClient
                .connectAsync(url, new StompSessionHandlerAdapter() {
                })
                .get(500, SECONDS);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void pingSubscription() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            System.out.println("i = " + i);
            String result = ping();
            assertNotNull(result);
            assertEquals("pong", result);
        }
    }

    private String ping() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        StompSession.Subscription subscription = session.subscribe("/app/ping", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload == null) {
                    log.info("null payload");
                } else {
                    log.info(payload.toString());
                    queue.clear();
                    queue.add(payload.toString());
                }
            }
        });
        String response = queue.poll(100, MILLISECONDS);
        subscription.unsubscribe();
        return response;
    }

}