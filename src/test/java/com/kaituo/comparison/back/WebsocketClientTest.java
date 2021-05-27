package com.kaituo.comparison.back;


import com.kaituo.comparison.back.core.websocket.DataEventTypeEnum;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 */
public class WebsocketClientTest {

    public static WebSocketClient client;

    public static void main(String[] args) {
        try {
            client = new WebSocketClient(new URI("ws://localhost:1001/websocket")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    client.send(DataEventTypeEnum.MYSELF.name());
                    System.out.println("打开链接");
                }

                @Override
                public void onMessage(String s) {
                    System.out.println("收到消息" + s);
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


    public static void send(byte[] bytes) {
        client.send(bytes);
    }

}
