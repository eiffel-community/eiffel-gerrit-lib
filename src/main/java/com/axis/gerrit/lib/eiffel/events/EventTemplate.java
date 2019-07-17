/*
 * Copyright (c) 2019, Axis Communications AB. All rights reserved.
 */
package com.axis.gerrit.lib.eiffel.events;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author christbi, Christian Bilevits
 * @since 2019-07-17
 */
public abstract class EventTemplate implements Template {

    protected JsonObject meta;
    protected JsonObject data;
    protected JsonArray links;

    protected Gson gson = new Gson();

    public EventTemplate(JsonObject eiffelTemplate) {
        JsonObject msgParams = eiffelTemplate.getAsJsonObject("msgParams");
        JsonObject eventParams = eiffelTemplate.getAsJsonObject("eventParams");
        this.meta = msgParams.getAsJsonObject("meta");
        this.data = eventParams.getAsJsonObject("data");
        this.links = eventParams.getAsJsonArray("links");
    }

    @Override
    public JsonObject generateEiffelEvent(JsonObject gerritEvent) {
        generateTemplate(gerritEvent);

        JsonObject event = new JsonParser().parse("{" +
                "\"msgParams\": {" +
                "\"meta\": " + gson.toJson(getEvent().getMeta()) +
                "},\"eventParams\": {" +
                "\"data\": " + getData() + "," +
                "\"links\": " + getLinks() + "}}").getAsJsonObject();

        return event;
    }
}
