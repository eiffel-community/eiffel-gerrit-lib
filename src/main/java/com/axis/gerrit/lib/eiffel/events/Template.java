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
package com.axis.gerrit.lib.eiffel.events;


import com.ericsson.eiffel.semantics.events.Event;
import com.google.gson.JsonObject;

/**
 * @author Christian Bilevits, christian.bilevits@axis.com
 * @since 2019-07-17
 */
public interface Template {

    /**
     * Generated the eiffel event from gerritEvent
     *
     * @param gerritEvent
     */
    void generateTemplate(JsonObject gerritEvent);

    /**
     * Generate the eiffel event if atleast meta is set.
     */
    void generateTemplate();

    /**
     * Genereated the eiffel event to a general JsonObject
     *
     * @param gerritEvent
     * @return JsonObject: Eiffel event
     */
    JsonObject generateEiffelEvent(JsonObject gerritEvent);

    /**
     * Sets the meta of the eiffel event
     *
     * @param meta Gerrit event
     */
    void setEventMeta(JsonObject meta);

    /**
     * Set the data of the eiffel event
     *
     * @param data Gerrit event
     */
    void setEventData(JsonObject data);

    /**
     * Set the links of the eiffel event
     *
     * @param target Gerrit event
     */
    void setLinksEvent(String target);

    /**
     * Returns the whole event
     *
     * @return Event
     */
    Event getEvent();

    /**
     * Get the data from the event as string
     *
     * @return String: Event data
     */
    String getData();

    /**
     * Get the links from the event as string
     *
     * @return String: Event links
     */
    String getLinks();
}
