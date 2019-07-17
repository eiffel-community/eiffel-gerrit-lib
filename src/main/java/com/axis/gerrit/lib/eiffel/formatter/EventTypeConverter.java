/*
 * Copyright (c) 2019, Axis Communications AB. All rights reserved.
 */
package com.axis.gerrit.lib.eiffel.formatter;

import com.ericsson.eiffel.remrem.semantics.EiffelEventType;

import java.util.HashMap;
import java.util.Map;

import static com.axis.gerrit.lib.eiffel.formatter.GerritEventType.CHANGE_MERGED;
import static com.axis.gerrit.lib.eiffel.formatter.GerritEventType.PATCHSET_CREATED;
import static com.ericsson.eiffel.remrem.semantics.EiffelEventType.SOURCECHANGE_CREATED;
import static com.ericsson.eiffel.remrem.semantics.EiffelEventType.SOURCECHANGE_SUBMITTED;

/**
 * @author christbi, Christian Bilevits
 * @since 2019-07-10
 */
public class EventTypeConverter {

    private static Map<GerritEventType, EiffelEventType> eventTypes = EventTypeConverter.eventType();

    public static Map<GerritEventType, EiffelEventType> eventType() {
        eventTypes = new HashMap<>();
        eventTypes.put(PATCHSET_CREATED, SOURCECHANGE_CREATED);
        eventTypes.put(CHANGE_MERGED, SOURCECHANGE_SUBMITTED);
        return eventTypes;
    }

    /**
     * Get matching eiffel event type name
     *
     * @param gerritEventType Gerrit event type
     * @return String: Eiffel event type
     */
    public String getEiffelEventName(GerritEventType gerritEventType) {
        return getEiffelEvent(gerritEventType).getEventName();
    }

    /**
     * Get eiffel event from gerrit event
     *
     * @param gerritEventType GerritEventType
     * @return EiffelEventType
     */
    public EiffelEventType getEiffelEvent(GerritEventType gerritEventType) {
        return eventTypes.get(gerritEventType);
    }

}
