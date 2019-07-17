/*
 * Copyright (c) 2019, Axis Communications AB. All rights reserved.
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
 * @author christbi, Christian Bilevits
 * @since 2019-07-17
 */
public class EiffelSourceChangeCreatedEventTemplate extends EventTemplate {

    private EiffelSourceChangeCreatedEvent event;
    private EiffelSourceChangeCreatedEventMeta metaEvent;
    private EiffelSourceChangeCreatedEventData dataEvent;

    private Source sourceEvent;
    private GitIdentifier gitIdentifierEvent;

    private EiffelSourceChangeCreatedEventTemplate(JsonObject eiffelTemplate) {
        super(eiffelTemplate);
    }

    public EiffelSourceChangeCreatedEventTemplate(SemanticsService service) {
        this(service.getEventTemplate(SOURCECHANGE_CREATED.getEventName()).getAsJsonObject());
        JsonObject gitIdentifier = super.data.getAsJsonObject("gitIdentifier");
        JsonObject source = super.meta.getAsJsonObject("source");

        this.gitIdentifierEvent = gson.fromJson(gitIdentifier, GitIdentifier.class);
        this.sourceEvent = gson.fromJson(source, Source.class);
        this.event = new EiffelSourceChangeCreatedEvent();
        this.metaEvent = new EiffelSourceChangeCreatedEventMeta();
        this.dataEvent = new EiffelSourceChangeCreatedEventData();
    }


    @Override
    public void generateTemplate(JsonObject gerritEvent) {
        setMetaEvent(gerritEvent);
        setDataEvent(gerritEvent);
        generateTemplate();
    }

    @Override
    public void generateTemplate() {
        if (metaEvent != null) {
            event.setMeta(metaEvent);
            if (dataEvent != null) {
                event.setData(dataEvent);
            }
        } else {
            throw new NullPointerException("Meta is not set");
        }
    }

    @Override
    public void setMetaEvent(JsonObject meta) {
        sourceEvent.setHost(meta.getAsJsonObject("change").get("url").toString());
        sourceEvent.setName(meta.getAsJsonObject("uploader").get("name").toString());
        metaEvent.setSource(sourceEvent);
    }

    @Override
    public void setDataEvent(JsonObject data) {
        JsonObject change = data.getAsJsonObject("change");
        gitIdentifierEvent.setCommitId(change.get("id").toString());
        gitIdentifierEvent.setBranch(change.get("branch").toString());
        gitIdentifierEvent.setRepoName(data.get("project").toString());
        gitIdentifierEvent.setRepoUri(change.get("url").toString());

        dataEvent.setGitIdentifier(gitIdentifierEvent);
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
