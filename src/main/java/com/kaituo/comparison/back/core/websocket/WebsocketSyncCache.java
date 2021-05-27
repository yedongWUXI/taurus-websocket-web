/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * Contributor license agreements.See the NOTICE file distributed with
 * This work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * he License.You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.kaituo.comparison.back.core.websocket;

import com.google.common.collect.Maps;
import com.kaituo.comparison.back.common.concurrent.SoulThreadFactory;
import com.kaituo.comparison.back.core.config.SoulConfig;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Websocket sync cache.
 *
 */
public class WebsocketSyncCache  {

    static final ConcurrentMap<String, String> MAP = Maps.newConcurrentMap();

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketSyncCache.class);

    /**
     * The Client.
     */
    private WebSocketClient client;

    private volatile boolean alreadySync = Boolean.FALSE;

    /**
     * Instantiates a new Websocket sync cache.
     *
     * @param websocketConfig the websocket config
     */
    public WebsocketSyncCache(final SoulConfig.WebsocketConfig websocketConfig) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1,
                SoulThreadFactory.create("websocket-connect", true));

        //开启webSocket连接
        try {
            client = new WebSocketClient(new URI(websocketConfig.getUrl())) {
                @Override
                public void onOpen(final ServerHandshake serverHandshake) {
                    if (!alreadySync) {
                        client.send(DataEventTypeEnum.MYSELF.name());
                        alreadySync = true;
                    }
                }

                @Override
                public void onMessage(final String result) {
                    try {
                        handleResult(result);
                    } catch (Exception e) {
                        LOGGER.error("websocket handle data exception :", e);
                    }
                }

                @Override
                public void onClose(final int code, final String msg, final boolean b) {
                    client.close();
                }

                @Override
                public void onError(final Exception e) {
                    client.close();
                }
            };
        } catch (URISyntaxException e) {
            LOGGER.error("websocket url is error :", e);
        }
        try {
            assert client != null;
            boolean success = client.connectBlocking(3000, TimeUnit.MILLISECONDS);
            if (success) {
                //项目启动打印的successful是在这
                LOGGER.info("websocket connection is successful.....");
            } else {
                LOGGER.error("websocket connection is error.....");
            }
        } catch (InterruptedException e) {
            LOGGER.info("websocket connection...exception....", e);
        }

        //初始10秒后执行,之后每30秒监测client的健康状态
        executor.scheduleAtFixedRate(() -> {
            try {
                if (client != null && client.isClosed()) {
                    //断开后重连
                    boolean success = client.reconnectBlocking();
                    if (success) {
                        LOGGER.info("websocket reconnect is successful.....");
                    } else {
                        LOGGER.error("websocket reconnection is error.....");
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.error("websocket connect is error :{}", e.getMessage());
            }

        }, 10, 30, TimeUnit.SECONDS);
    }

    private void handleResult(final String result) {


        LOGGER.info("消费websocket, 数据:{}", result);
        LOGGER.info("{}", "写入网关内存");
    }
}
