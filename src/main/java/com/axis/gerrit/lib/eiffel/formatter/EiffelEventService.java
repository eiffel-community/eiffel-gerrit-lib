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
import com.ericsson.eiffel.remrem.semantics.validator.EiffelValidationException;
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
    public JsonObject convertToEiffel(JsonObject gerritEvent) throws EventException, EiffelValidationException {
        String gerritType = getGerritType(gerritEvent);
        checkEventTypeSupported(gerritType);
        Template eiffelEvent = eventTemplates.get(GerritEventType.fromString(gerritType));
        return generateEiffelEvent(eiffelEvent, gerritType, gerritEvent);
    }

    /**
     * Get Eiffel event format from Gerrit event and set link id.
     *
     * @param gerritEvent Gerrit event
     * @param eiffelId Old Eiffel event id
     * @return JsonObject: Eiffel event
     */
    public JsonObject convertToEiffel(JsonObject gerritEvent, String eiffelId)
            throws EventException, EiffelValidationException {
        String gerritType = getGerritType(gerritEvent);
        checkEventTypeSupported(gerritType);
        Template eiffelEvent = generateEiffelTemplate(gerritType);
        eiffelEvent.setLinksEvent(eiffelId);
        return generateEiffelEvent(eiffelEvent, gerritType, gerritEvent);
    }

    private JsonObject generateEiffelEvent(Template eiffelEvent, String gerritType, JsonObject gerritEvent)
            throws EiffelValidationException {
        String message = service.generateMsg(getEiffelType(gerritType), eiffelEvent.generateEiffelEvent(gerritEvent));
        return validatedEiffelEvent(gerritType, new JsonParser().parse(message).getAsJsonObject());
    }

    private JsonObject validatedEiffelEvent(String gerritType, JsonObject event) throws EiffelValidationException {
        if (!service.validateMsg(getEiffelType(gerritType), event).isValid())
            throw new EiffelValidationException("Event failed validation");
        return event;
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

    private String getGerritType(JsonObject gerritEvent) throws EventException {
        if (gerritEvent.get("type") == null) throw new EventException("Event type is missing.");
        return gerritEvent.get("type").toString().replace("\"", "");
    }

    /**
     * Gets the id of an eiffel event
     * @param event Eiffel event
     * @return String: Eiffel ID
     */
    public String getEiffelEventId(JsonObject event){
        return service.getEventId(event);
    }

    private void checkEventTypeSupported(String gerritEventType) throws EventException {
        if (GerritEventType.fromString(gerritEventType) == null)
            throw new EventException("Gerrit event type \"" + gerritEventType + "\" is not supported yet.");
    }
}
