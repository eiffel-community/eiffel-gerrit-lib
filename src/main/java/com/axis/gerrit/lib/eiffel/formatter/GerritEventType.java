/*
 * Copyright 2019 Axis Communications AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axis.gerrit.lib.eiffel.formatter;

import java.util.HashMap;

/**
 * @author Christian Bilevits, christian.bilevits@axis.com
 * @since 2019-07-17
 */
public enum GerritEventType {
    PATCHSET_CREATED("patchset-created"),
    CHANGE_MERGED("change-merged");

    static HashMap<String, GerritEventType> eventTypeMap = new HashMap<>();
    private String eventType;

    GerritEventType(String eventType) {
        this.eventType = eventType;
    }

    public static GerritEventType fromString(String eventType) {
        if (eventTypeMap.size() == 0) {
            for (GerritEventType type : values()) {
                eventTypeMap.put(type.eventType.toLowerCase(), type);
            }
        }
        return eventTypeMap.get(eventType.toLowerCase());
    }

    public String getEventName() {
        return eventType;
    }

}
