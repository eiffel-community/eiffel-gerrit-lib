package com.axis.eiffel.gerrit.lib.formatter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Elizaveta Chigrina, elizaveta.chigrina@gmail.com
 * @since 2020-05-01
 */
public enum TestResourceType {
    EIFFEL_CREATED_EVENT("/testdata/eiffelCreatedEvent.json"),
    EIFFEL_SUBMITTED_EVENT("/testdata/eiffelSubmittedEvent.json"),
    GERRIT_CREATED_EVENT("/testdata/gerritCreatedEvent.json"),
    GERRIT_MERGED_EVENT("/testdata/gerritMergedEvent.json"),
    GERRIT_MERGED_EVENT_FALSE("/testdata/gerritMergedEventFalse.json");

    private final String path;

    TestResourceType(String path) {
        this.path = path;
    }

    public JsonObject load() {
        try {
            return new JsonParser()
                    .parse(IOUtils.toString(getClass().getResource(path), StandardCharsets.UTF_8))
                    .getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + path + " file");
        }
    }
}
