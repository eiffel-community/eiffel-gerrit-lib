/*
 * Copyright (c) 2019, Axis Communications AB. All rights reserved.
 */
package com.axis.gerrit.lib.eiffel.formatter;


import com.axis.gerrit.lib.eiffel.events.EiffelSourceChangeCreatedEventTemplate;
import com.axis.gerrit.lib.eiffel.events.EiffelSourceChangeSubmittedEventTemplate;
import com.axis.gerrit.lib.eiffel.events.Template;
import com.ericsson.eiffel.remrem.semantics.SemanticsService;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import static com.axis.gerrit.lib.eiffel.formatter.GerritEventType.CHANGE_MERGED;
import static com.axis.gerrit.lib.eiffel.formatter.GerritEventType.PATCHSET_CREATED;

/**
 * @author chrillebile, Christian Bilevits
 * @since 2019-07-09
 */
public class EiffelEventService {

    private static final SemanticsService service = new SemanticsService();
    private static final EventTypeConverter converter = new EventTypeConverter();
    private Map<GerritEventType, Template> eventTemplates;

    public EiffelEventService() {
        eventTemplates = eventTemplate();
    }

    public Map<GerritEventType, Template> eventTemplate() {
        eventTemplates = new HashMap<>();
        eventTemplates.put(PATCHSET_CREATED, new EiffelSourceChangeCreatedEventTemplate(service));
        eventTemplates.put(CHANGE_MERGED, new EiffelSourceChangeSubmittedEventTemplate(service));
        return eventTemplates;
    }

    /***
     * Get Eiffel event format from Gerrit event.
     * @param gerritEvent Gerrit event
     * @return String: Eiffel event
     */
    public String getEiffel(JsonObject gerritEvent) {
        String gerritType = getGerritType(gerritEvent);
        Template eiffelEvent = eventTemplates.get(GerritEventType.fromString(gerritType));
        return generateEiffelMsg(eiffelEvent, gerritType, gerritEvent);
    }

    /**
     * Get Eiffel event format from Gerrit event and set link id
     *
     * @param gerritEvent Gerrit event
     * @param eiffelId Old eiffel event
     * @return String: Eiffel event
     */
    public String getEiffel(JsonObject gerritEvent, String eiffelId) {
        String gerritType = getGerritType(gerritEvent);
        Template eiffelEvent = generateEiffelTemplate(gerritType);
        eiffelEvent.setLinksEvent(eiffelId);
        return generateEiffelMsg(eiffelEvent, gerritType, gerritEvent);
    }

    private String generateEiffelMsg(Template eiffelEvent, String gerritType, JsonObject gerritEvent) {
        return service.generateMsg(getEiffelType(gerritType), eiffelEvent.generateEiffelEvent(gerritEvent));
    }

    private Template generateEiffelTemplate(String gerritType) {
        return eventTemplates.get(GerritEventType.fromString(gerritType));
    }

    /***
     * Get the matching Eiffel event from Gerrit event
     * @param gerritType Gerrit event type
     * @return String: Eiffel event type
     */
    public String getEiffelType(String gerritType) {
        GerritEventType type = GerritEventType.fromString(gerritType);
        return converter.getEiffelEventName(type);
    }

    private String getGerritType(JsonObject gerritEvent) {
        return gerritEvent.get("type").toString().replace("\"", "");
    }
}
