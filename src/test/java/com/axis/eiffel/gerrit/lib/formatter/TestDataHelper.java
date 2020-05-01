package com.axis.eiffel.gerrit.lib.formatter;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Elizaveta Chigrina, elizaveta.chigrina@gmail.com
 * @since 2020-05-01
 */
public final class TestDataHelper {

    enum ResourceType {
        EIFFEL_CREATED_EVENT("/testdata/eiffelCreatedEvent.json"),
        EIFFEL_SUBMITTED_EVENT("/testdata/eiffelSubmittedEvent.json"),
        GERRIT_CREATED_EVENT("/testdata/gerritCreatedEvent.json"),
        GERRIT_MERGED_EVENT("/testdata/gerritMergedEvent.json"),
        GERRIT_MERGED_EVENT_FALSE("/testdata/gerritMergedEventFalse.json");

        private final String path;

        ResourceType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public String getResourceAsString(ResourceType resourceType) {
        try {
            return IOUtils.toString(getClass().getResource(resourceType.getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + resourceType.name() + " file");
        }
    }
}
