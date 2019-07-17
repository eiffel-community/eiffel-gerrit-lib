/*
 * Copyright (c) 2019, Axis Communications AB. All rights reserved.
 */
package com.axis.gerrit.lib.eiffel.formatter;

import java.util.HashMap;

/**
 * @author christbi, Christian Bilevits
 * @since 2019-07-10
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
