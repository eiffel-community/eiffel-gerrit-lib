/*
 * Copyright (c) 2019, Axis Communications AB. All rights reserved.
 */

package com.axis.gerrit.lib.eiffel.events;


import com.ericsson.eiffel.semantics.events.Event;
import com.google.gson.JsonObject;

/**
 * @author christbi, Christian Bilevits
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
    void setMetaEvent(JsonObject meta);

    /**
     * Set the data of the eiffel event
     *
     * @param data Gerrit event
     */
    void setDataEvent(JsonObject data);

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
