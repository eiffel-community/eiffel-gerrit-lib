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

import com.ericsson.eiffel.remrem.semantics.SemanticsService;
import com.ericsson.eiffel.semantics.events.EiffelSourceChangeSubmittedEvent;
import com.ericsson.eiffel.semantics.events.EiffelSourceChangeSubmittedEventData;
import com.ericsson.eiffel.semantics.events.EiffelSourceChangeSubmittedEventMeta;
import com.ericsson.eiffel.semantics.events.GitIdentifier;
import com.ericsson.eiffel.semantics.events.Link;
import com.ericsson.eiffel.semantics.events.Source;
import com.google.gson.JsonObject;

import java.util.Collections;

import static com.ericsson.eiffel.remrem.semantics.EiffelEventType.SOURCECHANGE_SUBMITTED;

/**
 * @author Christian Bilevits, christian.bilevits@axis.com
 * @since 2019-07-17
 */
public class EiffelSourceChangeSubmittedEventTemplate extends EventTemplate {

    private EiffelSourceChangeSubmittedEvent event;
    private EiffelSourceChangeSubmittedEventMeta eventMeta;
    private EiffelSourceChangeSubmittedEventData eventData;

    private Source source;
    private GitIdentifier gitIdentifier;
    private Link link;

    private EiffelSourceChangeSubmittedEventTemplate(JsonObject eiffelTemplate) {
        super(eiffelTemplate);
    }

    public EiffelSourceChangeSubmittedEventTemplate(SemanticsService service) {
        this(service.getEventTemplate(SOURCECHANGE_SUBMITTED.getEventName()).getAsJsonObject());
        JsonObject gitIdentifier = super.data.getAsJsonObject("gitIdentifier");
        JsonObject source = super.meta.getAsJsonObject("source");

        this.gitIdentifier = gson.fromJson(gitIdentifier, GitIdentifier.class);
        this.source = gson.fromJson(source, Source.class);
        init();
    }

    private void init() {
        this.link = new Link();
        this.event = new EiffelSourceChangeSubmittedEvent();
        this.eventMeta = new EiffelSourceChangeSubmittedEventMeta();
        this.eventData = new EiffelSourceChangeSubmittedEventData();
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
            if (link != null) {
                event.setLinks(Collections.singletonList(link));
            }
        } else {
            throw new NullPointerException("Meta is not set.");
        }
    }

    @Override
    public void setEventMeta(JsonObject meta) {
        JsonObject change = meta.getAsJsonObject("change");
        JsonObject patchSet = meta.getAsJsonObject("patchSet");
        JsonObject uploader = patchSet.getAsJsonObject("uploader");
        source.setHost(change.get("url").toString());
        source.setName(uploader.get("name").toString());
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
        link.setType("CHANGE");
        link.setTarget(target);
    }

    @Override
    public EiffelSourceChangeSubmittedEvent getEvent() {
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

    @Override
    protected void resetValues() {
        init();
    }
}
