/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.kaituo.comparison.back.core.config;

import com.kaituo.comparison.back.core.websocket.WebsocketSyncCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author huangxiaofeng
 * @author xiaoyu
 */
@EnableConfigurationProperties(SoulConfig.class)
public class LocalCacheConfiguration {


    /**
     * The type Websocket cache manager.
     */
    @Configuration
    @ConditionalOnProperty(name = "soul.sync.strategy", havingValue = "websocket", matchIfMissing = true)
    static class WebsocketCacheManager {

        /**
         * 项目启动时连接webSocket  并初始化jvm内存
         *
         * @param soulConfig the soul config
         * @return the local cache manager
         */
        @Bean
        public WebsocketSyncCache localCacheManager(final SoulConfig soulConfig) {
            return new WebsocketSyncCache(soulConfig.getSync().getWebsocket());
        }
    }

}
