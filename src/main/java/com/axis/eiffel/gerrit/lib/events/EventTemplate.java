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
package com.axis.eiffel.gerrit.lib.events;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Christian Bilevits, christian.bilevits@axis.com
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
