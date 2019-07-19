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

import com.ericsson.eiffel.remrem.semantics.SemanticsService;
import com.ericsson.eiffel.semantics.events.EiffelSourceChangeCreatedEvent;
import com.ericsson.eiffel.semantics.events.EiffelSourceChangeCreatedEventData;
import com.ericsson.eiffel.semantics.events.EiffelSourceChangeCreatedEventMeta;
import com.ericsson.eiffel.semantics.events.GitIdentifier;
import com.ericsson.eiffel.semantics.events.Source;
import com.google.gson.JsonObject;

import static com.ericsson.eiffel.remrem.semantics.EiffelEventType.SOURCECHANGE_CREATED;

/**
 * @author Christian Bilevits, christian.bilevits@axis.com
 * @since 2019-07-17
 */
public class EiffelSourceChangeCreatedEventTemplate extends EventTemplate {

    private EiffelSourceChangeCreatedEvent event;
    private EiffelSourceChangeCreatedEventMeta eventMeta;
    private EiffelSourceChangeCreatedEventData eventData;

    private Source source;
    private GitIdentifier gitIdentifier;

    private EiffelSourceChangeCreatedEventTemplate(JsonObject eiffelTemplate) {
        super(eiffelTemplate);
    }

    public EiffelSourceChangeCreatedEventTemplate(SemanticsService service) {
        this(service.getEventTemplate(SOURCECHANGE_CREATED.getEventName()).getAsJsonObject());
        JsonObject gitIdentifier = super.data.getAsJsonObject("gitIdentifier");
        JsonObject source = super.meta.getAsJsonObject("source");

        this.gitIdentifier = gson.fromJson(gitIdentifier, GitIdentifier.class);
        this.source = gson.fromJson(source, Source.class);
        this.event = new EiffelSourceChangeCreatedEvent();
        this.eventMeta = new EiffelSourceChangeCreatedEventMeta();
        this.eventData = new EiffelSourceChangeCreatedEventData();
    }


    @Override
    public void generateTemplate(JsonObject gerritEvent) {
        setEventMeta(gerritEvent);
        setEventData(gerritEvent);
        generateTemplate();
    }

    @Override
    public void generateTemplate() {
        if (eventMeta != null) {
            event.setMeta(eventMeta);
            if (eventData != null) {
                event.setData(eventData);
            }
        } else {
            throw new NullPointerException("Meta is not set");
        }
    }

    @Override
    public void setEventMeta(JsonObject meta) {
        source.setHost(meta.getAsJsonObject("change").get("url").toString());
        source.setName(meta.getAsJsonObject("uploader").get("name").toString());
        eventMeta.setSource(source);
    }

    @Override
    public void setEventData(JsonObject data) {
        JsonObject change = data.getAsJsonObject("change");
        gitIdentifier.setCommitId(change.get("id").toString());
        gitIdentifier.setBranch(change.get("branch").toString());
        gitIdentifier.setRepoName(data.get("project").toString());
        gitIdentifier.setRepoUri(change.get("url").toString());

        eventData.setGitIdentifier(gitIdentifier);
    }

    @Override
    public void setLinksEvent(String target) {

    }

    @Override
    public EiffelSourceChangeCreatedEvent getEvent() {
        return event;
    }

    @Override
    public String getData() {
        return gson.toJson(event.getData());
    }

    @Override
    public String getLinks() {
        return gson.toJson(event.getLinks());
    }
}
