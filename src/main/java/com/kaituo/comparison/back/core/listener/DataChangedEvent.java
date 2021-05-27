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

package com.kaituo.comparison.back.core.listener;

import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * Data change event.
 *
 */
public class DataChangedEvent extends ApplicationEvent {

    private String eventType;

    private String groupKey;

    /**
     * Instantiates a new Data changed event.
     *
     * @param groupKey the group key
     * @param type     the type
     * @param source   the source
     */
    public DataChangedEvent(final String groupKey, final String type, final List<?> source) {
        super(source);
        this.eventType = type;
        this.groupKey = groupKey;
    }

    /**
     * Gets event type.
     *
     * @return the event type
     */
    String getEventType() {
        return eventType;
    }

    @Override
    public List<?> getSource() {
        return (List<?>) super.getSource();
    }

    /**
     * Gets group key.
     *
     * @return the group key
     */
    public String getGroupKey() {
        return this.groupKey;
    }

}
