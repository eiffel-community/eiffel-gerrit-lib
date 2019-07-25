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


import com.axis.gerrit.lib.eiffel.events.EiffelSourceChangeCreatedEventTemplate;
import com.axis.gerrit.lib.eiffel.events.EiffelSourceChangeSubmittedEventTemplate;
import com.axis.gerrit.lib.eiffel.events.Template;
import com.ericsson.eiffel.remrem.semantics.SemanticsService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import static com.axis.gerrit.lib.eiffel.formatter.GerritEventType.CHANGE_MERGED;
import static com.axis.gerrit.lib.eiffel.formatter.GerritEventType.PATCHSET_CREATED;

/**
 * Service for converting Gerrit event to Eiffel event.
 *
 * @author Christian Bilevits, christian.bilevits@axis.com
 * @since 2019-07-17
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

    /**
     * Get Eiffel event format from Gerrit event.
     *
     * @param gerritEvent Gerrit event
     * @return JsonObject: Eiffel event
     */
    public JsonObject convertToEiffel(JsonObject gerritEvent) {
        String gerritType = getGerritType(gerritEvent);
        Template eiffelEvent = eventTemplates.get(GerritEventType.fromString(gerritType));
        return new JsonParser().parse(generateEiffelMsg(eiffelEvent, gerritType, gerritEvent)).getAsJsonObject();
    }

    /**
     * Get Eiffel event format from Gerrit event and set link id
     *
     * @param gerritEvent Gerrit event
     * @param eiffelId Old Eiffel event
     * @return JsonObject: Eiffel event
     */
    public JsonObject convertToEiffel(JsonObject gerritEvent, String eiffelId) {
        String gerritType = getGerritType(gerritEvent);
        Template eiffelEvent = generateEiffelTemplate(gerritType);
        eiffelEvent.setLinksEvent(eiffelId);
        return new JsonParser().parse(generateEiffelMsg(eiffelEvent, gerritType, gerritEvent)).getAsJsonObject();
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
