/*
 * Copyright (c) 2019, Axis Communications AB. All rights reserved.
 */
package com.axis.gerrit.lib.eiffel.events;

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
 * @author christbi, Christian Bilevits
 * @since 2019-07-17
 */
public class EiffelSourceChangeSubmittedEventTemplate extends EventTemplate {

    private EiffelSourceChangeSubmittedEvent event;
    private EiffelSourceChangeSubmittedEventMeta metaEvent;
    private EiffelSourceChangeSubmittedEventData dataEvent;

    private Source sourceEvent;
    private GitIdentifier gitIdentifierEvent;
    private Link linkEvent;

    private EiffelSourceChangeSubmittedEventTemplate(JsonObject eiffelTemplate) {
        super(eiffelTemplate);
    }

    public EiffelSourceChangeSubmittedEventTemplate(SemanticsService service) {
        this(service.getEventTemplate(SOURCECHANGE_SUBMITTED.getEventName()).getAsJsonObject());
        JsonObject gitIdentifier = super.data.getAsJsonObject("gitIdentifier");
        JsonObject source = super.meta.getAsJsonObject("source");

        this.gitIdentifierEvent = gson.fromJson(gitIdentifier, GitIdentifier.class);
        this.sourceEvent = gson.fromJson(source, Source.class);

        this.linkEvent = new Link();
        this.event = new EiffelSourceChangeSubmittedEvent();
        this.metaEvent = new EiffelSourceChangeSubmittedEventMeta();
        this.dataEvent = new EiffelSourceChangeSubmittedEventData();

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
            if (linkEvent != null) {
                event.setLinks(Collections.singletonList(linkEvent));
            }
        } else {
            throw new NullPointerException("Meta is not set");
        }
    }

    @Override
    public void setMetaEvent(JsonObject meta) {
        sourceEvent.setHost(meta.getAsJsonObject("change").get("url").toString());
        sourceEvent.setName(meta.getAsJsonObject("patchSet").getAsJsonObject("uploader").get("name").toString());
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
        linkEvent.setType("CHANGE");
        linkEvent.setTarget(target);
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
}
