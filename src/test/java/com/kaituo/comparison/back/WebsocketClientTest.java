package com.kaituo.comparison.back;


import com.kaituo.comparison.back.common.bean.DataEventTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 测试  开始webSocket
 */
@Slf4j
public class WebsocketClientTest {

    public static WebSocketClient client;

    public static void main(String[] args) {
        try {
            client = new WebSocketClient(new URI("ws://localhost:1001/websocket")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    client.send(DataEventTypeEnum.INIT.name());
                    System.out.println("打开链接");
                }

                @Override
                public void onMessage(String s) {

                    try {
                        handleResult(s);
                    } catch (Exception e) {
                        log.error("websocket handle data exception :", e);
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("链接已关闭");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    System.out.println("发生错误已关闭");
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        client.connect();

    }


    static void handleResult(final String result) {


        log.info("消费websocket, 数据:{}", result);
        log.info("{}", "写入网关内存");
    }
}
